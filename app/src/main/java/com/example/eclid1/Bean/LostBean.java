package com.example.eclid1.Bean;

import java.util.List;

public class LostBean {


    /**
     * code : 200
     * data : [{"username":"侯凯尧","phone":"17715501187","item":"手机","image":"http://192.168.3.14/image/a.jpg","datetime":"2021-09-16","place":"苏州市吴中区","remark":"丢失了，找不到了，请联系我的电话"},{"username":"周润泽","phone":"1231321","item":"钱","image":"http://192.168.3.14/image/b.jpg","datetime":"2021-09-22","place":"学校","remark":"没了啊啊啊 "},{"username":"黄璐寒","phone":"18065095727","item":"100W","image":"http://192.168.3.14/image/c.jpg","datetime":"2021-09-22","place":"银行门口路","remark":"啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊"},{"username":"houkayiao","phone":"17715501187","item":"U盘","image":"https://api.sziep.xyz/image/Lost/17715501187/1632983108.jpg","datetime":"2021-09-30","place":"图书馆","remark":"好人一生平安"}]
     * count : 4
     */
    private int code;
    private String count;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * username : 侯凯尧
         * phone : 17715501187
         * item : 手机
         * image : http://192.168.3.14/image/a.jpg
         * datetime : 2021-09-16
         * place : 苏州市吴中区
         * remark : 丢失了，找不到了，请联系我的电话
         */

        private String username;
        private String phone;
        private String item;
        private String image;
        private String datetime;
        private String place;
        private String remark;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getItem() {
            return item;
        }

        public void setItem(String item) {
            this.item = item;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getDatetime() {
            return datetime;
        }

        public void setDatetime(String datetime) {
            this.datetime = datetime;
        }

        public String getPlace() {
            return place;
        }

        public void setPlace(String place) {
            this.place = place;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }
}
