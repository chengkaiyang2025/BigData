package com.atguigu.apitest.sink;

import com.atguigu.apitest.beans.SensorReading;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Random;

public class MysqlSink {
    public static void main(String[] args) throws Exception {
        //自动生成数据源
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        DataStreamSource<SensorReading> source = env.addSource(new SourceFunction<SensorReading>() {
            boolean isRunning = true;
            Random r = new Random();

            @Override
            public void run(SourceContext<SensorReading> sourceContext) throws Exception {
                while (isRunning) {
                    sourceContext.collect(new SensorReading("sensor_" + r.nextInt(5)
                            , Double.valueOf(35 + r.nextGaussian())));
                    Thread.sleep(2000);
                }
            }

            @Override
            public void cancel() {
                this.isRunning = false;
            }
        });
        //写入到mysql
        source.addSink(new RichSinkFunction<SensorReading>() {
            Connection connection;
            PreparedStatement insert;
            PreparedStatement update;
            @Override
            public void open(Configuration parameters) throws Exception {
//                super.open(parameters);
                connection = DriverManager.getConnection("jdbc:mysql://172.38.0.138:3306/flink_tutorial","root","root");
                update = connection.prepareStatement("update sensor_temp set temp = ? where id = ? ");
                insert = connection.prepareStatement("insert into sensor_temp (id,temp) values (?, ?) ");

            }

            @Override
            public void close() throws Exception {
                insert.close();
                update.close();
                connection.close();
//                super.close();
            }

            @Override
            public void invoke(SensorReading value, Context context) throws Exception {
                update.setDouble(1, value.getTem());
                update.setString(2,value.getName());
                update.execute();
                if(update.getUpdateCount() == 0){
                    insert.setString(1,value.getName());
                    insert.setDouble(2,value.getTem());
                    insert.execute();
                }
            }
        });
        source.print();
        env.execute();
    }
}
