package edu.washington.escience.myriad.api.encoding;

import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;

import edu.washington.escience.myriad.RelationKey;
import edu.washington.escience.myriad.operator.DbInsert;
import edu.washington.escience.myriad.operator.Operator;
import edu.washington.escience.myriad.parallel.Server;

/**
 * A JSON-able wrapper for the expected wire message for a new dataset.
 * 
 */
public class DbInsertEncoding extends OperatorEncoding<DbInsert> {
  /** The name under which the dataset will be stored. */
  public RelationKey relationKey;
  /** The source of tuples to be inserted. */
  public String argChild;
  /** Whether to overwrite an existing dataset. */
  public Boolean argOverwriteTable;
  private static final List<String> requiredArguments = ImmutableList.of("relationKey", "argChild");

  @Override
  public void connect(final Operator current, final Map<String, Operator> operators) {
    current.setChildren(new Operator[] { operators.get(argChild) });
  }

  @Override
  public DbInsert construct(Server server) {
    if (argOverwriteTable != null) {
      return new DbInsert(null, relationKey, argOverwriteTable);
    }
    return new DbInsert(null, relationKey, false);
  }

  @Override
  protected List<String> getRequiredArguments() {
    return requiredArguments;
  }
}