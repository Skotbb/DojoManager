/**
 * Created by Scott on 10/15/2016.
 */

public class Rank {
    private enum RankType {Dan, Kyu};

    private int rankLevel;
    private int rankLevMin;
    private int rankLevMax;
    private RankType rank;
    private double timeInRank;

    public Rank(int rankLevel, RankType rankType) {
        this.rankLevel = rankLevel;
        this.rank = rankType;
        this.timeInRank = 0.0;

        setRankLevelMinAndMax(1, 10);
    }

    public Rank(int rankLevel, RankType rankType, double timeInRank) {
        this.rankLevel = rankLevel;
        this.rank = rankType;
        this.timeInRank = timeInRank;

        setRankLevelMinAndMax(1, 10);
    }

    public int getRankLevel() {
        return rankLevel;
    }

    public void setRankLevel(int rankLevel) {
        if(rankLevel >= rankLevMin && rankLevel <= rankLevMax){
            this.rankLevel = rankLevel;
        }else{
            throw new IllegalArgumentException("Rank needs to be between " + rankLevMin + " and " + rankLevMax + ", inclusive.");
        }
    }

    public RankType getRank() {
        return rank;
    }

    public void setRank(RankType rank) {
        this.rank = rank;
    }

    public double getTimeInRank() {
        return timeInRank;
    }

    public void setTimeInRank(double timeInRank) {
        if(timeInRank > 0 && timeInRank < 1000.0){
            this.timeInRank = timeInRank;
        }else{
            throw new IllegalArgumentException("The amount of time is invalid. Must be between 1 and 1000");
        }
    }

    public void addTimeInRank(double addTime){
        if(addTime > 0 && addTime < 1000.0){
            timeInRank += addTime;
        }else{
            throw new IllegalArgumentException("The amount of time is invalid. Must be between 1 and 1000");
        }
    }

    public void setRankLevelMinAndMax(int min, int max){
        if(min >= 0 && min < max && max <= 50) {
            this.rankLevMin = min;
            this.rankLevMax = max;
        }
    }

    public void promote(){
            //If Kyu rank and lower than the minimum number, decrement number
        if(rank.equals(RankType.Kyu) && rankLevel > rankLevMin){
            rankLevel--;
        }
            //If Kyu rank and equal to minimum number, change to Dan rank
        else if(rank.equals(RankType.Kyu) && rankLevel == rankLevMin){
            rank = RankType.Dan;
            rankLevel = 1;
        }
            //If Dan rank, and less than max number, increment number
        if(rank.equals(RankType.Dan) && rankLevel < rankLevMax){
            rankLevel++;
        }
    }

    @Override
    public String toString() {
        StringBuffer str = new StringBuffer();

        switch(rankLevel){
            case 1: str.append("1st " + rank);
                break;
            case 2: str.append("2nd " + rank);
                break;
            case 3: str.append("3rd " + rank);
                break;
            default: str.append(rankLevel+"th " + rank);
        }
        return str.toString();
    }
}
