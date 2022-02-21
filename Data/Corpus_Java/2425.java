package best_time_to_buy_and_sell_stock_iii;

public class BestTimetoBuyandSellStockIII {

    public class Solution {
        public int maxProfit(int[] prices) {
            if (prices.length == 0) {
                return 0;
            }
            int[] profit = new int[prices.length];
            profit[0] = 0;
            int minPrice = prices[0];
            for (int i = 1; i < prices.length; i++) {
                profit[i] = Math.max(profit[i - 1], prices[i] - minPrice);
                minPrice = Math.min(minPrice, prices[i]);
            }
            int maxPrice = prices[prices.length - 1];
            int maxProfit = profit[prices.length - 1];
            for (int i = prices.length - 2; i > 0; i--) {
                maxProfit = Math.max(maxProfit, profit[i - 1] + maxPrice
                        - prices[i]);
                maxPrice = Math.max(maxPrice, prices[i]);
            }
            return maxProfit;
        }
    }

    public static class UnitTest {

    }
}
