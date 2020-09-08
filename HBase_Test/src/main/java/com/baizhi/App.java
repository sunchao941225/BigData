package com.baizhi;

import com.yammer.metrics.stats.EWMA;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class App {
    @Test
    public void test01() {

        System.out.println(1);
    }

    private Configuration configuration;
    private Connection conn;
    private Admin admin;

    @Before
    public void getClient() throws Exception {
        configuration = new Configuration();

        configuration.set("hbase.zookeeper.quorum", "HadoopNode00");
        configuration.set("hbase.zookeeper.property.clientPort", "2181");
        conn = ConnectionFactory.createConnection(configuration);
        admin = conn.getAdmin();

    }


    @Test
    public void createNameSpace() throws Exception {

        NamespaceDescriptor namespaceDescriptor = NamespaceDescriptor.create("baizhi").addConfiguration("admin", "gjf").build();

        admin.createNamespace(namespaceDescriptor);

    }

    @Test
    public void listNameSpace() throws Exception {

        NamespaceDescriptor[] listNamespaceDescriptors = admin.listNamespaceDescriptors();


        for (NamespaceDescriptor listNamespaceDescriptor : listNamespaceDescriptors) {


            System.out.println(listNamespaceDescriptor.getName());
        }


    }

    @Test
    public void modifyNameSpace() throws Exception {

        NamespaceDescriptor namespaceDescriptor = NamespaceDescriptor.create("baizhi123").addConfiguration("aa", "bb").removeConfiguration("admin").build();


        admin.modifyNamespace(namespaceDescriptor);


    }

    @Test
    public void deleteNameSpace() throws Exception {
        admin.deleteNamespace("baizhi123");

    }

    @Test

    public void createTables() throws Exception {

        /*
        创建表名的对象（封装表名字）
        * */
        TableName tableName = TableName.valueOf("baizhi:t_user02");


        /*
         * 封装  表 的相关信息
         * */


        HTableDescriptor hTableDescriptor = new HTableDescriptor(tableName);

        /*
         * 封装列簇的相关信息
         * */
        HColumnDescriptor cf1 = new HColumnDescriptor("cf1");
        cf1.setMaxVersions(3);


        /*
         *
         * */
        HColumnDescriptor cf2 = new HColumnDescriptor("cf2");
        cf2.setMaxVersions(3);

        /*
         * 在hTableDescriptor 对象中添加列簇描述对象
         * */


        hTableDescriptor.addFamily(cf1);
        hTableDescriptor.addFamily(cf2);


        /*
         * 创建 table
         * */
        admin.createTable(hTableDescriptor);

    }

    @Test

    public void dropTable() throws Exception {


        TableName tableName = TableName.valueOf("baizhi:t_user1");
        admin.disableTable(tableName);
        admin.deleteTable(tableName);
    }


    @Test
    public void testPutOne() throws Exception {

        TableName tableName = TableName.valueOf("baizhi:t_user");
        /*
         * 通过conn对象获得table的操作对象
         * */
        Table table = conn.getTable(tableName);

        Put put1 = new Put("1".getBytes());
        put1.addColumn("cf1".getBytes(), "name".getBytes(), "zhangsan".getBytes());
        put1.addColumn("cf1".getBytes(), "age".getBytes(), "18".getBytes());
        put1.addColumn("cf1".getBytes(), "sex".getBytes(), "false".getBytes());


        table.put(put1);
        table.close();

    }

    @Test
    public void testPutList() throws Exception {

        TableName tableName = TableName.valueOf("baizhi:t_user");
        BufferedMutator bufferedMutator = conn.getBufferedMutator(tableName);


        Put put1 = new Put("4".getBytes());
        put1.addColumn("cf1".getBytes(), "name".getBytes(), "zs".getBytes());
        put1.addColumn("cf1".getBytes(), "age".getBytes(), "19".getBytes());
        put1.addColumn("cf1".getBytes(), "sex".getBytes(), "true".getBytes());

        Put put2 = new Put("5".getBytes());
        put2.addColumn("cf1".getBytes(), "name".getBytes(), "zhangsan".getBytes());
        put2.addColumn("cf1".getBytes(), "age".getBytes(), "18".getBytes());
        put2.addColumn("cf1".getBytes(), "sex".getBytes(), "false".getBytes());

        Put put3 = new Put("6".getBytes());
        put3.addColumn("cf1".getBytes(), "name".getBytes(), "zhangsan".getBytes());
        put3.addColumn("cf1".getBytes(), "age".getBytes(), "18".getBytes());
        put3.addColumn("cf1".getBytes(), "sex".getBytes(), "false".getBytes());

        ArrayList<Put> puts = new ArrayList<Put>();
        puts.add(put1);
        puts.add(put2);
        puts.add(put3);

        bufferedMutator.mutate(puts);

        bufferedMutator.close();

    }

    @Test
    public void tetsDelete() throws Exception {

        TableName tableName = TableName.valueOf("baizhi:t_user");
        Table table = conn.getTable(tableName);
        Delete delete = new Delete("6".getBytes());

        table.delete(delete);
        table.close();

    }


    @Test
    public void testDeleteList() throws Exception {


        TableName tableName = TableName.valueOf("baizhi:t_user");
        BufferedMutator bufferedMutator = conn.getBufferedMutator(tableName);

        Delete delete1 = new Delete("1".getBytes());
        Delete delete2 = new Delete("2".getBytes());
        Delete delete3 = new Delete("3".getBytes());

        ArrayList<Delete> deletes = new ArrayList<Delete>();

        deletes.add(delete1);
        deletes.add(delete2);
        deletes.add(delete3);

        bufferedMutator.mutate(deletes);

        bufferedMutator.close();


    }


    @Test

    public void testGet() throws Exception {

        TableName tableName = TableName.valueOf("baizhi:t_user");
        Table table = conn.getTable(tableName);

        Get get = new Get("4".getBytes());


        Result result = table.get(get);

        byte[] name = result.getValue("cf1".getBytes(), "name".getBytes());
        byte[] age = result.getValue("cf1".getBytes(), "age".getBytes());
        byte[] sex = result.getValue("cf1".getBytes(), "sex".getBytes());

        System.out.println(new String(name) + "-" + new String(age) + "-" + new String(sex));

    }


    @Test
    public void testGet02() throws Exception {
        /*
        *
        * hbase(main):012:0>  t.get '4',{COLUMN=>'cf1:name',VERSIONS=>3}
            COLUMN                        CELL
            cf1:name                     timestamp=1569284691440, value=zs
            cf1:name                     timestamp=1569283965094, value=zhangsan

        * */

        TableName tableName = TableName.valueOf("baizhi:t_user");
        Table table = conn.getTable(tableName);

        Get get = new Get("4".getBytes());
        get.setMaxVersions(2);

        get.addColumn("cf1".getBytes(), "name".getBytes());

        Result result = table.get(get);

        List<Cell> cellList = result.getColumnCells("cf1".getBytes(), "name".getBytes());


        for (Cell cell : cellList) {
            /*
             * rowkey 列名  列值  时间戳
             * */


            byte[] rowkey = CellUtil.cloneRow(cell);
            byte[] cf = CellUtil.cloneFamily(cell);
            byte[] qualifier = CellUtil.cloneQualifier(cell);
            byte[] value = CellUtil.cloneValue(cell);
            long timestamp = cell.getTimestamp();

            System.out.println(new String(rowkey) + "--" + new String(cf) + "--" + new String(qualifier) + "--" + new String(value) + "--" + timestamp);


        }


    }

    @Test
    public void testScan() throws Exception {

        TableName tableName = TableName.valueOf("baizhi:t_user");
        Table table = conn.getTable(tableName);

        Scan scan = new Scan();


        PrefixFilter prefixFilter1 = new PrefixFilter("4".getBytes());
        PrefixFilter prefixFilter2 = new PrefixFilter("5".getBytes());

        FilterList list = new FilterList(FilterList.Operator.MUST_PASS_ONE,prefixFilter1, prefixFilter2);
        scan.setFilter(list);


        ResultScanner results = table.getScanner(scan);

        for (Result result : results) {

            byte[] row = result.getRow();
            byte[] name = result.getValue("cf1".getBytes(), "name".getBytes());
            byte[] age = result.getValue("cf1".getBytes(), "age".getBytes());
            byte[] sex = result.getValue("cf1".getBytes(), "sex".getBytes());

            System.out.println(new String(row) + "--" + new String(name) + "-" + new String(age) + "-" + new String(sex));
        }


    }



    @Test
    public void testPut03() throws Exception {
        TableName tname = TableName.valueOf("baizhi:t_user02");

        BufferedMutator mb = conn.getBufferedMutator(tname);


        String[] company = {"baidu", "ali", "sina"};
        List<Put> list = new ArrayList<>();
        for (int i = 1; i < 1000; i++) {

            //得到随机的公司名称
            String rowkeyP = company[new Random().nextInt(3)];


            String empid = "";
            if (i < 10) {



                empid = "00" + i;
            } else if (i < 100) {
                empid = "0" + i;
            } else {

                empid = "" + i;

            }


            //  baidu:001
            String rowkey = rowkeyP + ":" + empid;

            Put put = new Put(rowkey.getBytes());
            put.addColumn("cf1".getBytes(), "name".getBytes(), "lisi1".getBytes());
            put.addColumn("cf1".getBytes(), "age".getBytes(), "281".getBytes());
            put.addColumn("cf1".getBytes(), "sex".getBytes(), "false".getBytes());
            put.addColumn("cf1".getBytes(), "salary".getBytes(), Bytes.toBytes(100.0 * i));
            list.add(put);
        }


        mb.mutate(list);
        mb.close();
    }


    @Test
    public void testPut04() throws Exception {
        TableName tname = TableName.valueOf("baizhi:t_user02");

        BufferedMutator mb = conn.getBufferedMutator(tname);


        String[] company = {"baidu", "ali", "sina"};
        List<Put> list = new ArrayList<>();
        for (int i = 1; i < 1000; i++) {

            String rowkeyP = company[new Random().nextInt(3)];


            String empid = "";
            if (i < 10) {


                empid = "00" + i;
            } else if (i < 100) {
                empid = "0" + i;
            } else {

                empid = "" + i;

            }

            String rowkey = rowkeyP + ":" + empid;

            Put put = new Put(rowkey.getBytes());
            put.addColumn("cf1".getBytes(), "name".getBytes(), "lisi1".getBytes());
            put.addColumn("cf1".getBytes(), "age".getBytes(), Bytes.toBytes(i + ""));

            put.addColumn("cf1".getBytes(), "salary".getBytes(), Bytes.toBytes(100.0 * i + ""));
            put.addColumn("cf1".getBytes(), "company".getBytes(), rowkeyP.getBytes());
            list.add(put);
        }


        mb.mutate(list);
        mb.close();
    }


    @After
    public void close() throws Exception {
        admin.close();
        conn.close();

    }
}
