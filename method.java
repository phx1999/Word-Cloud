import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dependency.nnparser.option.TestOption;
import com.hankcs.hanlp.seg.CRF.CRFSegment;
import com.hankcs.hanlp.seg.Dijkstra.DijkstraSegment;
import com.hankcs.hanlp.seg.NShort.NShortSegment;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.IndexTokenizer;
import com.hankcs.hanlp.tokenizer.NLPTokenizer;
import com.hankcs.hanlp.tokenizer.SpeedTokenizer;
import com.hankcs.hanlp.tokenizer.StandardTokenizer;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;

public class method {
    public static List getkeywords(String a, int b) {
        List<String> keywordList = HanLP.extractKeyword(a, b);
        return keywordList;
    }

    public static List fenci(String a) {
        List<Term> termList = StandardTokenizer.segment(a);
        return termList;
    }

    public static String readFile(File file){
        StringBuilder result = new StringBuilder();
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
                result.append(System.lineSeparator()+s);
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return result.toString();
    }

    public static List getcharacter(List b) {
        List<String> chinese = new ArrayList<>();
        for (int i = 0; i < b.size(); i++) {
            Object a = b.get(i);
            String c = String.valueOf(a);
            String[] strs=c.split("/");

            chinese.add(strs[0]);
        }
        return chinese;
    }

    public static List controlsize(List a) {
        List<String> x = new ArrayList<>();
        for (int i = 0; i < a.size(); i++) {
            Object b = a.get(i);
            String c = String.valueOf(b);
            if (c.length() >= 2) {
                x.add(c);
            }
        }
        return x;
    }
    public static List [] collection(List<String > a,List<Integer> b){
        Map<String, Integer> map = new HashMap<String, Integer>();
        for (int i=0;i<a.size();i++){
            map.put(a.get(i),b.get(i));
        }
        List<Map.Entry<String,Integer>> list=new ArrayList<>();
        list.addAll(map.entrySet());
        ValueComparator vc=new ValueComparator();
        Collections.sort(list,vc);
        System.out.println(list);
        List<String> x = new ArrayList<>();
        List<Integer> y = new ArrayList<>();
        for (int i=0;i<list.size();i++){
            x.add(list.get(i).getKey());
            y.add(list.get(i).getValue());
        }
        List [] ci=new List[2];
        ci[0]=x;
        ci[1]=y;
        return ci;
    }
    private static class ValueComparator implements Comparator<Map.Entry<String,Integer>>
    {
        public int compare(Map.Entry<String,Integer> m,Map.Entry<String,Integer> n)
        {
            return n.getValue()-m.getValue();
        }
    }

    public static List<Integer> wordsnumber(List a, List b) {
        List<Integer> number=new ArrayList<>();
        for (int i = 0; i < a.size(); i++) {
            int num = 0;
            for (int j = 0; j < b.size(); j++) {
                if (a.get(i).equals(b.get(j))) {
                    num = num + 1;
                }
            }
            number.add(num);
        }
        return number;
    }

    public static List quchu(List a) {
        List<String> x = new ArrayList<>();
        for (int i = 0; i < a.size(); i++) {
            Object b = a.get(i);
            String c = String.valueOf(b);
            String str = c.replaceAll("[`qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……& amp;*（）——+|{}【】‘；：”“’。，、？|-]", "");
            x.add(str);
        }
        return x;
    }

    public static void creatjpg(JFrame jf) {
        //得到窗口内容面板
        Container content=jf.getContentPane();
        //创建缓冲图片对象
        BufferedImage img=new BufferedImage(
                jf.getWidth(),jf.getHeight(),BufferedImage.TYPE_INT_RGB);
        //得到图形对象
        Graphics2D g2d = img.createGraphics();
        //将窗口内容面板输出到图形对象中
        content.printAll(g2d);
        //保存为图片
        File f=new File("11.jpg");
        try {
            ImageIO.write(img, "jpg", f);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //释放图形对象
        g2d.dispose();
    }
    public static void cutJPG(InputStream input, OutputStream out, int x,
                              int y, int width, int height) throws IOException {
        ImageInputStream imageStream = null;
        try {
            Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName("jpg");
            ImageReader reader = readers.next();
            imageStream = ImageIO.createImageInputStream(input);
            reader.setInput(imageStream, true);
            ImageReadParam param = reader.getDefaultReadParam();

            System.out.println(reader.getWidth(0));
            System.out.println(reader.getHeight(0));
            Rectangle rect = new Rectangle(x, y, width, height);
            param.setSourceRegion(rect);
            BufferedImage bi = reader.read(0, param);
            ImageIO.write(bi, "jpg", out);
        } finally {
            imageStream.close();
        }
    }

}

