    package com.baizhi.hdfs;

    import java.io.BufferedReader;
    import java.io.File;
    import java.io.FileReader;
    import java.io.FileWriter;

    public class CleanApp {

        public static void main(String[] args) throws Exception {

            File file = new File("G:\\Note\\Day02-Hadoop\\数据文件\\access.tmp2019-05-19-10-28.log");

            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

            FileWriter fileWriter = new FileWriter("G:\\Note\\Day02-Hadoop\\数据文件\\clean.log");


            while (true) {

                String line = bufferedReader.readLine();

                if (line == null) {
                    bufferedReader.close();
                    fileWriter.close();
                    return;
                }

                boolean contains = line.contains("thisisshortvideoproject'slog");
                if (contains) {

                    String s = line.split("thisisshortvideoproject'slog")[0];
                    fileWriter.write(s.trim() + "\n");
                    fileWriter.flush();

                }

            }

        }
    }
