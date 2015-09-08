import java.io.*;
import java.lang.reflect.Array;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;



public class MP1 {
    Random generator;
    String userName;
    String inputFileName;
    String delimiters = " \t,;.?!-:@[](){}_*/";
    String[] stopWordsArray = {"i", "me", "my", "myself", "we", "our", "ours", "ourselves", "you", "your", "yours",
            "yourself", "yourselves", "he", "him", "his", "himself", "she", "her", "hers", "herself", "it", "its",
            "itself", "they", "them", "their", "theirs", "themselves", "what", "which", "who", "whom", "this", "that",
            "these", "those", "am", "is", "are", "was", "were", "be", "been", "being", "have", "has", "had", "having",
            "do", "does", "did", "doing", "a", "an", "the", "and", "but", "if", "or", "because", "as", "until", "while",
            "of", "at", "by", "for", "with", "about", "against", "between", "into", "through", "during", "before",
            "after", "above", "below", "to", "from", "up", "down", "in", "out", "on", "off", "over", "under", "again",
            "further", "then", "once", "here", "there", "when", "where", "why", "how", "all", "any", "both", "each",
            "few", "more", "most", "other", "some", "such", "no", "nor", "not", "only", "own", "same", "so", "than",
            "too", "very", "s", "t", "can", "will", "just", "don", "should", "now"};
    HashMap<String,Integer> wordCnt;
    HashSet<String> wordSet;
    HashMap<Integer, Integer> lineMap;
    

    void initialRandomGenerator(String seed) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA");
        messageDigest.update(seed.toLowerCase().trim().getBytes());
        byte[] seedMD5 = messageDigest.digest();

        long longSeed = 0;
        for (int i = 0; i < seedMD5.length; i++) {
            longSeed += ((long) seedMD5[i] & 0xffL) << (8 * i);
        }

        this.generator = new Random(longSeed);
    }

    Integer[] getIndexes() throws NoSuchAlgorithmException {
        Integer n = 10000;
        Integer number_of_lines = 50000;
        Integer[] ret = new Integer[n];
        this.initialRandomGenerator(this.userName);
        for (int i = 0; i < n; i++) {
            ret[i] = generator.nextInt(number_of_lines);
        }
        return ret;
    }

    public MP1(String userName, String inputFileName) {
        this.userName = userName;
        this.inputFileName = inputFileName;
        wordSet = new HashSet<String>();
        for (String e:stopWordsArray) {
            wordSet.add(e);
        }

        
    }
    
    static final Comparator<Map.Entry<String, Integer>> entry_compare = new Comparator<Map.Entry<String, Integer>>() {
        public int compare(Map.Entry<String, Integer> a, Map.Entry<String, Integer> b) {
            if (a.getValue() != b.getValue())
                return b.getValue() - a.getValue();
            
            return a.getKey().compareTo(b.getKey());
        }
        
    };


    public String[] process() throws Exception {
        String[] ret = new String[20];
        
        //TODO
        Integer[] index = getIndexes();
        lineMap = new HashMap<Integer, Integer>();

        Integer cnt = null;
        for (Integer i:index) {
            cnt = lineMap.get(i);
            if (cnt == null) {
                lineMap.put(i, 1);
            } else {
                lineMap.put(i, cnt+1);
            }
        }
        
        wordCnt = new HashMap<String,Integer>();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(this.inputFileName)));
        int line_num = -1;
        Integer line_cnt = null;
        String data = null;
        
        while ((data = br.readLine()) != null) {
            line_num++;
            line_cnt = lineMap.get(line_num);
            
            if (line_cnt == null)
                continue;

            if (data.trim().equals(""))
                continue;

            data = data.toLowerCase();
            StringTokenizer st = new StringTokenizer(data, delimiters);
            while (st.hasMoreTokens()) {
                String key = st.nextToken();
                if (key != null && !wordSet.contains(key)) {
                    cnt = wordCnt.get(key);
                    if (cnt == null) {
                        wordCnt.put(key, line_cnt);
                    } else {
                        wordCnt.put(key, cnt+line_cnt);
                    }
                }
                    
            }

        }
        //System.out.println(line_num+ " map:"+wordCnt.size());
        br.close();
        
        PriorityQueue<Map.Entry<String, Integer>> heap = new PriorityQueue<Map.Entry<String, Integer>>(20, entry_compare);
        Iterator<Map.Entry<String, Integer>> iter = wordCnt.entrySet().iterator();

        Map.Entry<String, Integer> entry;

        while (iter.hasNext()) {
            entry = iter.next();
            heap.add(entry);
        }
        
        int idx = 0;
        
        while (!heap.isEmpty() && idx < 20) {
            entry = heap.poll();
            ret[idx++] = entry.getKey();
            //System.out.println(entry.getKey() + " " + entry.getValue());
        }
        

        return ret;
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 1){
            System.out.println("MP1 <User ID>");
        }
        else {
            String userName = args[0];
            String inputFileName = "./input.txt";
            MP1 mp = new MP1(userName, inputFileName);
            String[] topItems = mp.process();
            for (String item: topItems){
                System.out.println(item);
            }
        }
    }
}
