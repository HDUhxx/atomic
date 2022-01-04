@Entry
@Component
struct Index {
  @State value: string = ''

  build() {
    Flex({ direction: FlexDirection.Column, alignItems: ItemAlign.Center, justifyContent: FlexAlign.SpaceBetween }) {
      Text('连续点击两次')
        .fontSize(20)
      Text(this.value)
        .fontSize(20)
    }
    .height(200).width(300).padding(60).border({ width: 1 }).margin(30)
    .gesture(
    TapGesture({ count: 2 })
      .onAction(() => {
        this.value = 'this TapGesture onAction'
      })
    )
  }
}
