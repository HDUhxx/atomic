
import prompt from '@system.prompt';

export default {
  data: {
    direction: 'column',
    list: []
  },
  onInit() {
    this.list = []
    this.listAdd = []
    for (var i = 1; i <= 4; i++) {
      var dataItem = {
        value: '第' + i + '班',
      };
      this.list.push(dataItem);
    }
  },
  collapseOne(e) {
    this.$element('mylist').collapseGroup({
      groupid: '第1班'
    })
  },
  expandOne(e) {
    this.$element('mylist').expandGroup({
      groupid: '第1班'
    })
  },
  collapsetwo(e) {
    this.$element('mylist').collapseGroup({
      groupid: '第2班'
    })
  },
  expandtwo(e) {
    this.$element('mylist').expandGroup({
      groupid: '第2班'
    })
  },
  collapsethree(e) {
    this.$element('mylist').collapseGroup({
      groupid: '第3班'
    })
  },
  expandthree(e) {
    this.$element('mylist').expandGroup({
      groupid: '第3班'
    })
  },
  collapsefour(e) {
    this.$element('mylist').collapseGroup({
      groupid: '第4班'
    })
  },
  expandfour(e) {
    this.$element('mylist').expandGroup({
      groupid: '第4班'
    })
  },
  collapseAll(e) {
    this.$element('mylist').collapseGroup()
  },
  expandAll(e) {
    this.$element('mylist').expandGroup()
  },
  collapse(e) {
    prompt.showToast({
      message: '关闭 ' + e.groupid
    })
  },
  expand(e) {

    prompt.showToast({
      message: '查看 ' + e.groupid

    })
  }


}
