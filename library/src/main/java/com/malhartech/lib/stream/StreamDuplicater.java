/*
 *  Copyright (c) 2012 Malhar, Inc.
 *  All Rights Reserved.
 */
package com.malhartech.lib.stream;

import com.malhartech.annotation.InputPortFieldAnnotation;
import com.malhartech.annotation.OutputPortFieldAnnotation;
import com.malhartech.api.BaseOperator;
import com.malhartech.api.DefaultInputPort;
import com.malhartech.api.DefaultOutputPort;

/**
 * Takes one stream and emits exactly same tuple on both the output ports. Needed to allow separation of listeners into two streams<p>
 * <br>
 * <br>
 * <b>Tuple Schema</b>: All tuples were treated as Object
 * <b>Port Interface</b><br>
 * <b>data</b>: Input port<br>
 * <b>out1</b>: Output port 1<br>
 * <b>out2</b>: Output port 2<br>
 * <br>
 * <b>Properties</b>:
 * None
 * <br>
 * Compile time checks are:<br>
 * no checks are done. Schema check is compile/instantiation time. Not runtime
 * <br>
 * <b>Benchmarks</b>: Blast as many tuples as possible in inline mode<br>
 * Operator does >400 million tuples/sec as all tuples simply forwarded as is<br>
 *
 * @author amol
 */
public class StreamDuplicater<K> extends BaseOperator
{
  @InputPortFieldAnnotation(name = "data")
  public final transient DefaultInputPort<K> data = new DefaultInputPort<K>(this)
  {
    @Override
    public void process(K tuple)
    {
      out1.emit(tuple);
      out2.emit(tuple);
    }
  };

  @OutputPortFieldAnnotation(name = "out1")
  public final transient DefaultOutputPort<K> out1 = new DefaultOutputPort<K>(this);
  @OutputPortFieldAnnotation(name = "out2")
  public final transient DefaultOutputPort<K> out2 = new DefaultOutputPort<K>(this);
  final static int num_oport = 2;

  /**
   * Allows usage of StreamDuplicater in a automated way
   *
   * @param i port index
   * @return returns the proper input port name
   */
  static public String getOutputName(int i)
  {
    String ret = "illegal_portnumber";
    if ((i != 0) && (i <= num_oport)) {
      ret = "out";
      ret += Integer.toString(i);
    }
    return ret;
  }

  public int getNumOutputPorts()
  {
    return num_oport;
  }
}
