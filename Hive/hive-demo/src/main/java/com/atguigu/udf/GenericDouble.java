package com.atguigu.udf;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.exec.UDFArgumentTypeException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDFUtils;
import org.apache.hadoop.hive.serde2.lazy.LazyDouble;
import org.apache.hadoop.hive.serde2.lazy.LazyInteger;
import org.apache.hadoop.hive.serde2.lazy.LazyString;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

@Description(
    name     = "GenericDouble",
    value    = "_FUNC_( value) : Double the value of numeric argument, " +
               "Concatinate value to itself for string arguments.",
    extended = "Example:\n" +
               "    SELECT _FUNC_(salary) FROM customers;\n" +
               "    (returns 12,000 if the salary was 6,000)\n\n" +
               "    SELECT _FUNC_(name) FROM customers;\n" +
               "    (returns \"Tim MayTim May\" if the name was \"Tim May\")\n"
)
/**
 * This class is a Generic User Defined Function meaning that we can call this
 * function with more than one type of arguments, i.e. int, long, float, double
 * and String. The function returns the same type of output as it gets the input.
 *
 * @author vpathak
 */
public final class GenericDouble extends GenericUDF {

    private ObjectInspector[] _inputObjectInspector = null;
    private GenericUDFUtils.ReturnObjectInspectorResolver
                           _returnObjectInspectorResolver = null;


    @Override
    public Object evaluate(DeferredObject[] arguments) throws HiveException {

        Object returnable = null;
        Object preparedOutput = null;
        String argumentType = arguments[0].get().getClass().getSimpleName();

        // System.out.println("Arguments[0]: Type: " + argumentType);

        // Check the type of argument ...
        if (argumentType.equalsIgnoreCase("LazyInteger"))    // select UDF(emp_no) from EMP
        {
            // The input parameter is an IntWritable ...
            LazyInteger lazyOut = new LazyInteger((LazyInteger) arguments[0].get());

            IntWritable underlyingInt = lazyOut.getWritableObject();
            underlyingInt.set( underlyingInt.get() * 2 );

            preparedOutput = lazyOut;
        }
        else if (argumentType.equalsIgnoreCase("IntWritable"))  // select UDF(9) from DUAL
        {
            // The input parameter is an IntWritable ...
            IntWritable inputParameter = (IntWritable) arguments[0].get();

            // Hive runs MR jobs in the background and Mappers/Reducers keep using
            // the same object as parameter, only the value is set() for iteration,
            // the Writable object remains the same. Therefore, we should be returning
            // a new object, instead of making changes in input Object's value.
            preparedOutput = new IntWritable( inputParameter.get() * 2 );

        }
        else if (argumentType.equalsIgnoreCase("LazyDouble"))    // select UDF(bonus) from EMP
        {
            // The input parameter is an IntWritable ...
            LazyDouble lazyOut = new LazyDouble((LazyDouble) arguments[0].get());

            DoubleWritable underlyingDouble = lazyOut.getWritableObject();
            underlyingDouble.set( underlyingDouble.get() * 2 );

            preparedOutput = lazyOut;
        }
        else if (argumentType.equalsIgnoreCase("DoubleWritable")) // select UDF(2.23) from dual;
        {
            // The input parameter is an DoubleWritable ...
            final DoubleWritable inputParameter = (DoubleWritable) arguments[0].get();

            // We should be returning a new object, instead of mutating the input.
            preparedOutput = new DoubleWritable( inputParameter.get() * 2 );
        }
        else if (argumentType.equalsIgnoreCase("LazyString"))    // select UDF(Job) from EMP
        {
            // The input parameter is a Wrapped Text ...
            LazyString lazyOut = new LazyString((LazyString) arguments[0].get());

            Text underlyingText = lazyOut.getWritableObject();
            underlyingText.set( underlyingText.toString() + underlyingText.toString() );

            preparedOutput = lazyOut;
        }
        else if (argumentType.equalsIgnoreCase("Text"))  // select UDF('Clerk') from dual
        {
            // The input parameter is an Text ...
            final Text inputParameter = (Text) arguments[0].get();

            // We should be returning a new object, instead of mutating the input.
            preparedOutput = new Text( inputParameter.toString() + inputParameter.toString() );
        }


        // Check input type (inputObjectInspector) and set the appropriate
        // output data type (outputValue) ...
        returnable = _returnObjectInspectorResolver.convertIfNecessary(preparedOutput, _inputObjectInspector[0]);

        return returnable;
    }


    @Override
    /**
     * This method is called within a Hive session (e.g.  hive> _ ) when the UDF is
     * called and an error occurs. The method gets a chance to put as much information
     * possible to show to the user. A standard string "HiveException: Error evaluating"
     * is prepended in front of whatever is returned by this function-
     *
     * e.g.  "HiveException: Error evaluating value in column emp_no (type: int)"
     */
    public String getDisplayString(String[] errorInfo)
    {
        return "value in column " + errorInfo[0] + " (type: " + _inputObjectInspector[0].getTypeName() + ").";
    }


    @Override
    /**
     * Called for each value of each column.
     */
    public ObjectInspector initialize(ObjectInspector[] arguments) throws UDFArgumentException {

        // Save the input Object Inspectors ...
        _inputObjectInspector = arguments;

        // Validate: Argument Count ...
        if (arguments.length <= 0 || arguments[0] == null) {
            throw new UDFArgumentException("No argument was detected.");
        }

        // Create the instance of the most important object within this class ...
        _returnObjectInspectorResolver = new GenericUDFUtils.ReturnObjectInspectorResolver(true);

        // Validate: Argument type checking ...
        if (_returnObjectInspectorResolver.update(arguments[0]) == false) {
            throw new UDFArgumentTypeException(2, "Datatype problem with the passed argument.");
        }

        return _returnObjectInspectorResolver.get();
    }
}