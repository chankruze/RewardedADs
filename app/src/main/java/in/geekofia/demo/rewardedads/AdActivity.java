package in.geekofia.demo.rewardedads;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class AdActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mAdButton, mTapButton;
    private TextView mMessage, mScore;
    private RewardedAd rewardedAd;
    private Activity mActivity;
    private int score = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad);

        mActivity = this;

        // Load AD button
        mAdButton = findViewById(R.id.btn_ad);
        mAdButton.setEnabled(false);
        mAdButton.setOnClickListener(this);

        mTapButton = findViewById(R.id.btn_tap);
        mTapButton.setOnClickListener(this);

        // Log text view
        mMessage = findViewById(R.id.tv_log);
        mScore = findViewById(R.id.score);

        rewardedAd = new RewardedAd(this,
                "ca-app-pub-3940256099942544/5224354917");

        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                // Ad successfully loaded.
                mMessage.append("Reward AD has been loaded & ready to be shown\n");
                mAdButton.setEnabled(true);
            }

            @Override
            public void onRewardedAdFailedToLoad(int errorCode) {
                // Ad failed to load.
                mMessage.append("Reward AD failed to load\n");
            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);

    }

    public RewardedAd createAndLoadRewardedAd() {
        RewardedAd rewardedAd = new RewardedAd(this,
                "ca-app-pub-3940256099942544/5224354917");
        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                // Ad successfully loaded.
                mMessage.append("Reward AD has been loaded & ready to be shown\n");
                mAdButton.setEnabled(true);
            }

            @Override
            public void onRewardedAdFailedToLoad(int errorCode) {
                // Ad failed to load.
                mMessage.append("Reward AD failed to load\n");
            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
        return rewardedAd;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ad:
                if (rewardedAd.isLoaded()) {
                    Activity activityContext = mActivity;
                    RewardedAdCallback adCallback = new RewardedAdCallback() {
                        @Override
                        public void onRewardedAdOpened() {
                            // Ad opened.
                            mMessage.append("Reward AD has been opened\n");
                        }

                        @Override
                        public void onRewardedAdClosed() {
                            // Ad closed.
                            mMessage.append("Reward AD has been closed\n");
                            rewardedAd = createAndLoadRewardedAd();
                        }

                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem reward) {
                            // User earned reward.
                            mMessage.append("You got 10 points!\n");
                            score += 10;
                            updateScore();
                        }

                        @Override
                        public void onRewardedAdFailedToShow(int errorCode) {
                            // Ad failed to display.
                            mMessage.append("Reward AD has been failed to show\n");
                        }
                    };
                    rewardedAd.show(activityContext, adCallback);
                } else {
                    Log.d("TAG", "The rewarded ad wasn't loaded yet.");
                }
                break;

            case R.id.btn_tap:
                score += 1;
                updateScore();
                break;
        }
    }

    private void updateScore() {
        mScore.setText(String.valueOf(score));
    }
}
