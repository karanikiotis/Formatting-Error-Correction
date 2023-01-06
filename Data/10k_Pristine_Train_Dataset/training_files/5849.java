package unique_binary_search_trees_ii;

import java.util.ArrayList;

import common.TreeNode;

public class UniqueBinarySearchTreesII {

    public class Solution {

        private ArrayList<TreeNode> generateTrees(int start, int end) {
            ArrayList<TreeNode> ans = new ArrayList<TreeNode>();
            if (start > end) {
                ans.add(null);
                return ans;
            }
            for (int i = start; i <= end; i++) {
                for (TreeNode left : generateTrees(start, i - 1)) {
                    for (TreeNode right : generateTrees(i + 1, end)) {
                        TreeNode p = new TreeNode(i);
                        p.left = left;
                        p.right = right;
                        ans.add(p);
                    }
                }
            }
            return ans;
        }

        public ArrayList<TreeNode> generateTrees(int n) {
            return generateTrees(1, n);
        }
    }

    public static class UnitTest {

    }
}
