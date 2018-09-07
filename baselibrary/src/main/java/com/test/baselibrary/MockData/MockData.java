package com.test.baselibrary.MockData;


import com.test.baselibrary.bean.response.FirstAdvertisingBean;
import com.test.baselibrary.bean.response.FirstCommodityBean;
import com.test.baselibrary.bean.response.MealInfoProdectBean;
import com.test.baselibrary.bean.response.MealOfPeopleBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by lady_zhou on 2018/1/22.
 */

public class MockData {
    public static final String[] titles = {"听肺癌患者家属倾述:人工智能如何守护爱与健康", "关于“体检报告”，这大概是最科学的一篇文章了",
            "没有时间来管理健康？健康对于企业家成了奢侈品么？"};
    public static final String[] pics = {"http://pic122.nipic.com/file/20170219/5108620_131659075759_2.jpg",
            "http://pic125.nipic.com/file/20170401/24978725_164736285000_2.jpg",
            "http://pic126.nipic.com/file/20170404/9054345_112106862000_2.jpg",
            "http://pic125.nipic.com/file/20170401/24978725_162029935000_2.jpg",
            "http://pic125.nipic.com/file/20170401/24978725_162029889000_2.jpg",
            "http://pic125.nipic.com/file/20170401/24978725_163256459000_2.jpg",
            "http://pic125.nipic.com/file/20170401/18648048_102324880038_2.jpg",
            "http://pic125.nipic.com/file/20170328/24978725_161922155000_2.jpg",
            "http://pic125.nipic.com/file/20170328/24978725_162512485000_2.jpg",
            "http://pic125.nipic.com/file/20170328/24978725_170855369000_2.jpg"};

    public static final String[] commodityTitles = {"关爱父母特惠体检套餐", "孕前检查套餐", "中老年全面健康检查", "都市精英体检套餐", "肿瘤十二项检查高端套餐", "成人高端体检套餐"};

    public static final String[] types = {"中青年，防癌筛查，亚健康", "防癌筛查", "甲状腺", "糖尿病、肝部筛查",
            "针对心脑血管、肿瘤、骨质酥松检查", "血型、肝肾功能、传染疾病、遗传疾病、慢性病筛查"};

    public static final String[] projectTitles = {"一般检查A", "内科", "男外科", "外眼", "眼底镜检查", "耳鼻咽喉科", "口腔科"};

    public static final String[] projectContent = {"身高、体重、体重指数、收缩压、舒张压", "病史、家族史、心率、心律、心音", "皮肤、脊柱、四肢关节",
            "外眼、眼科其他", "眼底镜检查", "既往史、外耳、外耳道、估摸、鼻腔、咽、扁桃体", "牙体、牙周"};

    public static final String[] oldType = {"中老年", "少年", "中青年", "老年", "男性", "女性"};
    public static final String[] oldpics = {"https://img-blog.csdn.net/20180416170057365?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2xhZHlfemhvdQ==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70",
            "https://img-blog.csdn.net/20180416170513164?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2xhZHlfemhvdQ==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70",
            "https://img-blog.csdn.net/20180416170538757?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2xhZHlfemhvdQ==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70",
            "https://img-blog.csdn.net/20180416170607124?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2xhZHlfemhvdQ==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70",
            "https://img-blog.csdn.net/20180416170637579?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2xhZHlfemhvdQ==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70",
            "https://img-blog.csdn.net/20180416170659808?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2xhZHlfemhvdQ==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70",
            "https://img-blog.csdn.net/20180416170726270?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2xhZHlfemhvdQ==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70"};

    public static final String[] sex = {"男", "女"};
    private static Random random = new Random();

    /**
     * 随机标题
     *
     * @return
     */
    private static String getRndNick() {
        return titles[random.nextInt(titles.length)];
    }

    /**
     * 随机图片
     *
     * @return
     */
    private static String getRndPic() {
        return pics[random.nextInt(pics.length)];
    }

    /**
     * 随机过去时
     *
     * @return
     */
    private static long getRndTime() {
        return System.currentTimeMillis() - random.nextInt(10) * 24 * 60 * 60 * 1000;
    }

    /**
     * 随机价格
     *
     * @return
     */
    private static double getRndPrice() {
        return Double.parseDouble(random.nextInt(10000) + 100 + "");
    }

    /**
     * 随机数
     *
     * @return
     */
    private static int getRndnumber() {
        return random.nextInt(10000) + 1;
    }

    /**
     * 随机类型
     *
     * @return
     */
    private static String getRndType() {
        return types[random.nextInt(types.length)];
    }

    private static String getCommodityTitles() {
        return commodityTitles[random.nextInt(commodityTitles.length)];
    }

    public static List<FirstAdvertisingBean> getDatas(int count) {
        List<FirstAdvertisingBean> datas = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for (int i = 0; i < count; i++) {
            FirstAdvertisingBean firstAdvertisingBean = new FirstAdvertisingBean();
            firstAdvertisingBean.setImageUrl(getRndPic());
            firstAdvertisingBean.setTitle(getRndNick());
            firstAdvertisingBean.setTime(sdf.format(getRndTime()));
            datas.add(firstAdvertisingBean);
        }
        return datas;
    }

    public static List<FirstCommodityBean> getCommodityDatas(int count, boolean isShow) {
        List<FirstCommodityBean> datas = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            FirstCommodityBean firstCommodityBean = new FirstCommodityBean();
            firstCommodityBean.setShow(isShow);
            firstCommodityBean.setImageUrl(getRndPic());
            firstCommodityBean.setPeopleNumber(getRndnumber());
            firstCommodityBean.setPrice(getRndPrice());
            firstCommodityBean.setSex(sex[random.nextInt(sex.length)]);
            firstCommodityBean.setTitle(getCommodityTitles());
            if (isShow) {
                firstCommodityBean.setType(getRndType());
            }

            datas.add(firstCommodityBean);
        }
        return datas;
    }

    public static List<MealInfoProdectBean> getProjecDatas(int count) {
        List<MealInfoProdectBean> datas = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            MealInfoProdectBean mealInfoProdectBean = new MealInfoProdectBean();
            mealInfoProdectBean.setTitle(projectTitles[random.nextInt(projectTitles.length)]);
            mealInfoProdectBean.setContent(projectContent[random.nextInt(projectContent.length)]);
            datas.add(mealInfoProdectBean);
        }
        return datas;
    }

    public static List<MealOfPeopleBean> getPeopleDatas(int count, boolean isShow) {
        List<MealOfPeopleBean> datas = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            MealOfPeopleBean mealOfPeopleBean = new MealOfPeopleBean();
            mealOfPeopleBean.setShow(isShow);
            if (isShow) {
                mealOfPeopleBean.setImageUrl(oldpics[random.nextInt(oldpics.length)]);
                mealOfPeopleBean.setIntroduction(types[random.nextInt(types.length)]);
                mealOfPeopleBean.setTitle(projectTitles[random.nextInt(projectTitles.length)]);
            } else {
                mealOfPeopleBean.setImageUrl(oldpics[random.nextInt(2)]);
                mealOfPeopleBean.setTitle(oldType[random.nextInt(2)]);
            }
            datas.add(mealOfPeopleBean);
        }

        return datas;
    }

}

