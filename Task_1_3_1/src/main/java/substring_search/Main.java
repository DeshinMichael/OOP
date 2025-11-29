package substring_search;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        List<Long> indices = SubstringSearcher.find("input.txt", "бра");
        System.out.println(indices);
    }
}
