package com.example.eclid1.Bean;

import java.util.List;

public class Bus_Ground {

    private String return_code;
    private String error_code;
    private List<ReturlListDTO> returl_list;

    public String getReturn_code() {
        return return_code;
    }

    public void setReturn_code(String return_code) {
        this.return_code = return_code;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public List<ReturlListDTO> getReturl_list() {
        return returl_list;
    }

    public void setReturl_list(List<ReturlListDTO> returl_list) {
        this.returl_list = returl_list;
    }

    public static class ReturlListDTO {
        private String title;
        private List<String> buslist;
        private double xydistance;
        private double lat;
        private double lng;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<String> getBuslist() {
            return buslist;
        }

        public void setBuslist(List<String> buslist) {
            this.buslist = buslist;
        }

        public double getXydistance() {
            return xydistance;
        }

        public void setXydistance(double xydistance) {
            this.xydistance = xydistance;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }
    }
}
