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
 * show days of the month
 * @author chenqiaoyin
 * @date 2021/09/10
 *
 */
export default {
    props:{
        year:{default:0},
        month:{default:0},
        selectDate:{default:"-1"},
        isDark:{default:0}
    },
    data() {
        return {
            dates: [],
            currentDate:-1,
            selectDateState:-1,
            firstStart:0,
            lastLine:6,
            lastEnd:6
        };
    },
    onInit(){
        this.getDates(this.month);
        this.$watch('month', 'getDates');
        this.$watch('year', 'changeYear');
    },
    /**
     * 初始化系统的当前日期，以及被选中日期
     */
    initCurrentDate(){
        this.currentDate = -1;
        this.selectDateState = -1;

        var now = new Date();
        var currentYear = now.getFullYear()
        var currentMonth = now.getMonth()+1;
        if(this.year == currentYear && this.month == currentMonth){
            this.currentDate = now.getDate();
            if(this.selectDate == "-1"){
                this.selectDateState = this.currentDate;
            }
        }
        if(this.selectDate != "-1"){
            try{
                let yy = Number.parseInt(this.selectDate.toString().split("-")[0]);
                let mm = Number.parseInt(this.selectDate.toString().split("-")[1]);
                if(this.year == yy && this.month == mm) {
                    this.selectDateState = Number.parseInt(this.selectDate.toString().split("-")[2]);
                }
            }catch(e){

            }
        }
    },
    /**
     * 获取星期几
     */
    getWeekDate(yy,mm,dd) {
        var now = new Date(yy,mm-1,dd);
        var day = now.getDay();
        return day;
    },
    changeYear(){
        this.getDates(this.month);
    },
    /**
     * 获取页面展示的日期数据
     */
    getDates(mm){
        var mYear = 0;
        mYear = this.year;
        var dateArray = [];
        let lastMonthLastDate = 0;
        switch(mm){
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
            {
                for (let i = 1;i <= 31; i++) {
                    dateArray.push(i);
                }
                if(mm == 1){
                    lastMonthLastDate = 31;
                }else if(mm == 3){
                    if (mYear % 4 == 0) {
                        lastMonthLastDate = 29;
                    }else{
                        lastMonthLastDate = 28;
                    }
                }else if(mm == 8){
                    lastMonthLastDate = 31;
                }else{
                    lastMonthLastDate = 30;
                }
                break;
            }
            case 2:
            {
                for (let i = 1;i <= 28; i++) {
                    dateArray.push(i);
                }
                if (mYear % 4 == 0) {
                    dateArray.push(29);
                }
                lastMonthLastDate = 31;
                break;
            }
            case 4:
            case 6:
            case 9:
            case 11:
            {
                for (let i = 1;i <= 30; i++) {
                    dateArray.push(i);
                }
                lastMonthLastDate = 31;
                break;
            }
        }
        var number = this.getWeekDate(mYear,mm,1);
        this.firstStart = number;
        var dateArrayNew = [];
        var dateLine=[];
        for(let i=0;i<number+dateArray.length;i++){
            if(i%7==0){
                if(dateLine.length > 0){
                    dateArrayNew.push(dateLine);
                }
                dateLine=[];
            }
            if(i<number){
                let bj = (++lastMonthLastDate)-number;
                dateLine.push(bj>0 ? bj : "");
            }else{
                if(dateArray[i-number]<10) {
                    dateLine.push(dateArray[i - number]);
                }else{
                    dateLine.push(dateArray[i - number]);
                }
            }
        }
        this.lastLine = dateArrayNew.length;
        this.lastEnd = dateLine.length-1;
        let nextMonthDate=1;
        for(let i=dateLine.length;i<7;i++){
            dateLine.push(nextMonthDate++);
        }
        dateArrayNew.push(dateLine);
        if(dateArrayNew.length<6){
            for(let j=dateArrayNew.length;j<6;j++){
                var line=[];
                for(let k=0;k<7;k++){
                    line.push(nextMonthDate++);
                }
                dateArrayNew.push(line);
            }
        }
        this.dates = dateArrayNew;
        this.initCurrentDate();
    },
    /**
     * 选中日期，执行的方法
     */
    selDate(date){
        if(date>0){
            this.selectDateState = date;
            this.$emit("selDate",{date:this.year+"-"+(this.month<10?"0"+this.month:this.month)+"-"+(this.selectDateState<10?"0"+this.selectDateState:this.selectDateState)})
        }
    }
}
