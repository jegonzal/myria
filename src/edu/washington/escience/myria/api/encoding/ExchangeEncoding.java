package edu.washington.escience.myria.api.encoding;

import java.util.List;
import java.util.Set;

import edu.washington.escience.myria.operator.Operator;
import edu.washington.escience.myria.parallel.ExchangePairID;

public abstract class ExchangeEncoding<E extends Operator> extends OperatorEncoding<E> {

  private Set<Integer> realWorkerIds;
  private List<ExchangePairID> realOperatorIds;

  protected final Set<Integer> getRealWorkerIds() {
    return realWorkerIds;
  }

  protected final void setRealWorkerIds(Set<Integer> w) {
    realWorkerIds = w;
  }

  protected final List<ExchangePairID> getRealOperatorIds() {
    return realOperatorIds;
  }

  protected final void setRealOperatorIds(List<ExchangePairID> operatorIds) {
    realOperatorIds = operatorIds;
  }
}