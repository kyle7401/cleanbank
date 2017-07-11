package cleanbank.viewmodel;

import java.util.List;

/**
 * Created by hyoseop on 2016-01-05.
 */
public class MoTimeTable {

        public static  class tt_header {
                String date;
                String day_of_week;

                public tt_header() {
                }

                public tt_header(String date, String day_of_week) {
                        this.date = date;
                        this.day_of_week = day_of_week;
                }

                public String getDate() {
                        return date;
                }

                public void setDate(String date) {
                        this.date = date;
                }

                public String getDay_of_week() {
                        return day_of_week;
                }

                public void setDay_of_week(String day_of_week) {
                        this.day_of_week = day_of_week;
                }
        }

        public MoTimeTable() {
        }

        public MoTimeTable(List<tt_header> time_table_header) {
                this.time_table_header = time_table_header;
        }

        public List<tt_header> getTime_table_header() {
                return time_table_header;
        }

        public void setTime_table_header(List<tt_header> time_table_header) {
                this.time_table_header = time_table_header;
        }

        List<tt_header> time_table_header;

        //      time_table
        List<time_table> time_table_list;

        public List<time_table> getTime_table_list() {
                return time_table_list;
        }

        public void setTime_table_list(List<time_table> time_table_list) {
                this.time_table_list = time_table_list;
        }

        public static class time_table {
                private String title;
                List<col> cols;

                public static class col {
                        private String start_time;
                        private String end_time;
                        private String state;

                        public String getState() {
                                return state;
                        }

                        public void setState(String state) {
                                this.state = state;
                        }

                        public String getStart_time() {
                                return start_time;
                        }

                        public void setStart_time(String start_time) {
                                this.start_time = start_time;
                        }

                        public String getEnd_time() {
                                return end_time;
                        }

                        public void setEnd_time(String end_time) {
                                this.end_time = end_time;
                        }
                }

                public time_table() {
                }

                public List<col> getCols() {
                        return cols;
                }

                public void setCols(List<col> cols) {
                        this.cols = cols;
                }

                public time_table(String title) {
                        this.title = title;
                }

                public String getTitle() {
                        return title;
                }

                public void setTitle(String title) {
                        this.title = title;
                }
        }
}
