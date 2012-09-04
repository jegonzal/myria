package edu.washington.escience.myriad.column;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.washington.escience.myriad.proto.TransportProto.ColumnMessage;

public class FloatColumnTest {

  @Test
  public void testProto() {
    FloatColumn original = new FloatColumn();
    original.put(1).put(2).put(5).put(11);
    ColumnMessage serialized = original.serializeToProto();
    FloatColumn deserialized = new FloatColumn(serialized);
    assertTrue(original.toString().equals(deserialized.toString()));
  }

}