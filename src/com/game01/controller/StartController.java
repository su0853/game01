package com.game01.controller;

import com.game01.SceneManager;

public class StartController {
    /** 点击「開始遊戲」切到房間页面 **/
    public void onStartGame() throws Exception {
        // 事先要在 Main.register 中注册过 "Room"
        SceneManager.switchTo("Room");
    }

    /** 点击「排行榜」切到排行页面 **/
    public void onShowRanking() throws Exception {
        SceneManager.switchTo("Ranking");
    }
}
