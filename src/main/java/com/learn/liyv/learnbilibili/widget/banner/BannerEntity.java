package com.learn.liyv.learnbilibili.widget.banner;

/**
 * Created by lyh on 2017/12/9.
 * Banner 模型类
 */

public class BannerEntity {
    public String title;
    public String img;
    public String link;

    public BannerEntity(String link, String title, String img) {
        this.link = link;
        this.title = title;
        this.img = img;
    }
}
