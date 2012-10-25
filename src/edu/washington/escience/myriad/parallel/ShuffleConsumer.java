package edu.washington.escience.myriad.parallel;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Objects;

import edu.washington.escience.myriad.DbException;
import edu.washington.escience.myriad.Schema;
import edu.washington.escience.myriad.operator.Operator;
import edu.washington.escience.myriad.table._TupleBatch;

/**
 * The consumer part of the Shuffle Exchange operator.
 * 
 * A ShuffleProducer operator sends tuples to all the workers according to some PartitionFunction, while the
 * ShuffleConsumer (this class) encapsulates the methods to collect the tuples received at the worker from multiple
 * source workers' ShuffleProducer.
 * 
 */
public class ShuffleConsumer extends Consumer {

  private static final long serialVersionUID = 1L;

  private boolean finish;

  // Used to remember which of the source workers have sent an end of stream
  // message.
  private final BitSet workerEOS;

  private final int[] sourceWorkers;
  private final HashMap<Integer, Integer> workerIdToIndex;
  private ShuffleProducer child;

  public ShuffleConsumer(final ShuffleProducer child, final ExchangePairID operatorID, final int[] workerIDs) {
    super(operatorID);

    Objects.requireNonNull(child);
    Objects.requireNonNull(operatorID);
    Objects.requireNonNull(workerIDs);

    this.child = child;
    sourceWorkers = workerIDs;
    workerIdToIndex = new HashMap<Integer, Integer>();
    int i = 0;
    for (final Integer w : sourceWorkers) {
      workerIdToIndex.put(w, i++);
    }
    workerEOS = new BitSet(workerIDs.length);
    finish = false;
  }

  @Override
  public void close() {
    super.close();
    workerEOS.clear();
  }

  @Override
  protected _TupleBatch fetchNext() throws DbException {
    if (!finish) {
      try {
        return getTuples();
      } catch (final InterruptedException e) {
        e.printStackTrace();
        throw new DbException(e.getLocalizedMessage());
      }
    }
    return null;
  }

  @Override
  public Operator[] getChildren() {
    return new Operator[] { child };
  }

  @Override
  public String getName() {
    return "shuffle_c";
  }

  @Override
  public Schema getSchema() {
    return child.getSchema();
  }

  /**
   * 
   * Retrieve a batch of tuples from the buffer of ExchangeMessages. Wait if the buffer is empty.
   * 
   * @return Iterator over the new tuples received from the source workers. Return <code>null</code> if all source
   *         workers have sent an end of file message.
   */
  _TupleBatch getTuples() throws InterruptedException {
    ExchangeTupleBatch tb = null;

    while (workerEOS.nextClearBit(0) < sourceWorkers.length) {
      tb = take(-1);
      if (tb.isEos()) {
        workerEOS.set(workerIdToIndex.get(tb.getWorkerID()));
      } else {
        return tb;
      }
    }
    // have received all the eos message from all the workers
    finish = true;

    return null;
  }

  @Override
  public void open() throws DbException {
    if (child != null) {
      child.open();
    }
    super.open();
  }

  @Override
  public void setChildren(final Operator[] children) {
    child = (ShuffleProducer) children[0];
  }

}
