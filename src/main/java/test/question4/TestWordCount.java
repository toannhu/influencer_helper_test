package test.question4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import scala.Tuple2;

/**
 *
 * @author toannd4
 */
public class TestWordCount {

    private static final String VIETNAMESE_REGEX = "[^a-z0-9A-Z_ÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂưăạảấầẩẫậắằẳẵặẹẻẽềềểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ]";
    private static JavaSparkContext jsc = null;

    public static List<String> mostUsedWord(String paragraph, int nWords) {
        List<String> input = Arrays.asList(paragraph.split("\n"));
        List<Tuple2<Long, String>> data = jsc.parallelize(input).flatMap(line -> Arrays.asList(line.split(" ")).iterator())
                .map(word -> word.replaceAll(VIETNAMESE_REGEX, "").toLowerCase().trim())
                .filter(word -> word.length() > 0)
                .mapToPair(word -> new Tuple2<String, Long>(word, 1l))
                .reduceByKey((v1, v2) -> v1 + v2)
                .mapToPair(x -> new Tuple2<Long, String>(x._2, x._1))
                .reduceByKey((String v1, String v2) -> v1 + "\t" + v2)
                .sortByKey(false).take(1);

        List<String> listMostUsedWord = new ArrayList();
        if (data.size() > 0) {
            listMostUsedWord = Arrays.asList(data.get(0)._2.split("\t"));
            Collections.sort(listMostUsedWord, new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return o1.charAt(0) - o2.charAt(0);
                }   
            });
        }
        
        List<String> listNWords = new ArrayList();
        int limit = listMostUsedWord.size() > nWords ? nWords : listMostUsedWord.size();
        for (int i = 0; i < limit; i++) {
            listNWords.add(listMostUsedWord.get(i));
        }
        return listNWords;
    }
    
    public static void main(String[] args) {
        SparkSession spark = SparkSession
                .builder()
                .appName("Most n-word count")
                .master("local[*]")
                .getOrCreate();

        jsc = new JavaSparkContext(spark.sparkContext());
        String text = "Nivea đang có chương trình khuyến mãi hấp dẫn. Hãy sử dụng Nivea, nhắc lại là rất hấp dẫn nha :)";
        mostUsedWord(text, 3).forEach(line -> System.out.println(line));

        spark.close();
    }
    
}
