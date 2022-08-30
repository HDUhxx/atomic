/*
 * Copyright (C) 2021 Chinasoft International
 *
 * Licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *
 *     http://license.coscl.org.cn/MulanPSL2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * calendar component
 * @author chenqiaoyin
 * @date 2021/09/10
 *
 */
export default {
    props:{
        isDark:{default:0},
        selectDate:{default:"-1"}
    },
    data() {
        return{
            title: "",
            year:0,
            month:0,
            monthList:[],
            currentIndex:0,
            selectDateState:"-1",
            weeks:["日", "一", "二", "三", "四", "五", "六"],
            selectMulti:{
                multitext:[],
                multitextselect:[],
                year:0,
                month:0
            }
        }
    },
    onInit() {
        this.selectDateState = this.selectDate;
        var now = new Date();
        this.year = now.getFullYear();
        this.month = now.getMonth()+1;
        this.monthList = [this.month,this.month+1,this.month-1];
        let year = [];
        for(let i=this.year-100;i<=this.year+100;i++){
            year.push(i);
        }
        this.selectMulti.multitext.push(year);
        let month = [];
        for(let i=1;i<=12;i++){
            month.push(i<10?"0"+i:i);
        }
        this.selectMulti.multitext.push(month);
        this.selectMulti.multitextselect.push(100);
        this.selectMulti.multitextselect.push(now.getMonth());

        this.selectMulti.year = this.year;
        this.selectMulti.month = this.month;
    },
    /**
     * swiper切换，调用方法
     */
    changeMonth(e){
        if(this.currentIndex==0 && e.index==2 || this.currentIndex==2 && e.index==1 || this.currentIndex==1 && e.index==0){
            this.month = this.month -1;
            if(this.month == 0){
                this.month = 12;
                this.year -= 1;
            }
            switch (e.index){
                case 0:{
                           this.monthList = [this.month,this.month+1,this.month-1];
                           break;
                       }
                case 1:{
                           this.monthList = [this.month-1,this.month,this.month+1];
                           break;
                       }
                case 2:{
                           this.monthList = [this.month+1,this.month-1,this.month];
                           break;
                       }
            }
        }else if(this.currentIndex==0 && e.index==1 || this.currentIndex==1 && e.index==2 || this.currentIndex==2 && e.index==0){
            this.month = this.month +1;
            if(this.month == 13){
                this.month = 1;
                this.year += 1;
            }
            switch (e.index){
                case 0:{
                           this.monthList = [this.month,this.month+1,this.month-1];
                           break;
                       }
                case 1:{
                           this.monthList = [this.month-1,this.month,this.month+1];
                           break;
                       }
                case 2:{
                           this.monthList = [this.month+1,this.month-1,this.month];
                           break;
                       }
            }
        }
        this.currentIndex = e.index;
    },
    /**
     * 选中日期，执行的方法
     */
    selDate(e){
        this.selectDateState = e.detail.date;
        this.$emit("selDate",{date:e.detail.date})
    },
    changeLastMonth(){
        this.$element("mySwiper").showPrevious();
    },
    changeNextMonth(){
        this.$element("mySwiper").showNext();
//        this.toCurrentDate();
    },
    toCurrentDate(){
        this.isClickToToday = true;
        var now = new Date();
        let year = now.getFullYear();
        let month = now.getMonth()+1;
        this.toSelectYearMonth(year,month);
    },
    /**
     * 选中某年某月，swiper数据变化
     */
    toSelectYearMonth(year,month){
        if(this.year<year || this.year==year && this.month<month){
            this.month = month - 1;
            if(this.month==0){
                this.month = 12;
                this.year = year -1;
            }else{
                this.year = year
            }
            this.$element("mySwiper").showNext();

        }else if(this.year>year || this.year==year && this.month>month){
            this.month = month + 1;
            if(this.month==13){
                this.month = 1;
                this.year = year +1;
            }else{
                this.year = year;
            }
            this.$element("mySwiper").showPrevious();
        }
    },
    selectYearMonth(){
        this.$element('simpledialog').show();
    },
    cancelDialog(){
        this.$element('simpledialog').close();
    },
    confirmlDialog(){
        this.$element('simpledialog').close();
        this.toSelectYearMonth(this.selectMulti.year,this.selectMulti.month);
    },
    /**
     * 弹框中picker组件数据变化执行方法
     */
    columnchange(e){
        if(e.column==0){
            this.selectMulti.year = Number.parseInt(e.newValue);
            this.selectMulti.multitextselect[0] = e.newSelected;
        }else if(e.column==1){
            this.selectMulti.month = Number.parseInt(e.newValue);
            this.selectMulti.multitextselect[1] = e.newSelected;
        }
    }

}
