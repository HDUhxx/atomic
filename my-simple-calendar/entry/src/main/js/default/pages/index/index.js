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
import prompt from '@system.prompt';
export default {
    data: {
        isDark:0
    },
    onInit() {

    },
    toCurrentDate(){
        this.$child('mycalendar').toCurrentDate();
    },
    /**
     * 选中日期，返回给调用者
     */
    selDate(e){
        prompt.showToast({
            message:"当前选中的日期时："+e.detail.date,
            duration:2000
        });
    },
    /**
     * 切换日历模式
     */
    setCalendarModel(){
        if(this.isDark == 0){
            this.isDark = 1;
        }else{
            this.isDark = 0;
        }
    }

}
