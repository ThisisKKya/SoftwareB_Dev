package com.example.toaccountornot.utils;

import android.util.Log;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.util.List;

public class Cards extends LitePalSupport {
    private String card;    //账户名
    private String remark;  //备注
    private int cardid;     //卡的id
    private double income;     //收入
    private double outcome;    //支出
    private double surplus;    //结余

    public void setCards(String card, String remark, int cardid, double income, double outcome, double surplus) {
        this.card = card;
        this.remark = remark;
        this.cardid = cardid;
        this.income = income;
        this.outcome = outcome;
        this.surplus = surplus;
    }

    public String getCard() {
        return card;
    }

    public int getCardid() {
        return cardid;
    }

    public double getIncome() {
        income = 0;
        List<Accounts> accounts = LitePal.where("card = ? and inorout = ?" ,card,"in")
                                    .find(Accounts.class);
        for (Accounts account : accounts) {
            income += account.getPrice();
        }
        return income;
    }

    public double getOutcome() {
        outcome = 0;
        List<Accounts> accounts = LitePal.where("card = ? and inorout = ?" ,card,"out")
                .find(Accounts.class);
        for (Accounts account : accounts) {
            outcome += account.getPrice();
        }
        return outcome;
    }

    public double getSurplus() {
        surplus = getIncome()-getOutcome();
        return surplus;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public void setCardid(int cardid) {
        this.cardid = cardid;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
