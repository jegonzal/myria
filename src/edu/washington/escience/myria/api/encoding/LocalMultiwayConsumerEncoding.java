package edu.washington.escience.myria.api.encoding;

import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;

import edu.washington.escience.myria.operator.Operator;
import edu.washington.escience.myria.parallel.LocalMultiwayConsumer;
import edu.washington.escience.myria.parallel.Server;
import edu.washington.escience.myria.util.MyriaUtils;

/**
 * A JSON-able wrapper for the expected wire message for a new dataset.
 * 
 */
public class LocalMultiwayConsumerEncoding extends AbstractConsumerEncoding<LocalMultiwayConsumer> {
  private static final List<String> requiredArguments = ImmutableList.of("argOperatorId");

  @Override
  public void connect(Operator operator, Map<String, Operator> operators) {
    /* Do nothing; no children. */
  }

  @Override
  public LocalMultiwayConsumer construct(Server server) {
    return new LocalMultiwayConsumer(null, MyriaUtils.getSingleElement(getRealOperatorIds()));
  }

  @Override
  protected List<String> getRequiredArguments() {
    return requiredArguments;
  }

}