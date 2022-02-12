// page1.ets
@Entry
@Component
struct AExample {
  @State scale: number = 1
  @State opacity: number = 1
  @State active: boolean = false
  build() {
    Column() {
      Navigator({ target: 'pages/index' ,type: NavigationType.Push}) {
        Image($rawfile("gz.jpg")).width("100%").height("100%")
      }
    }.height("100%").width("100%").scale({ x: this.scale }).opacity(this.opacity)
  }
// 自定义方式1：完全自定义转场过程的效果
  pageTransition() {
    PageTransitionEnter({ duration: 1200, curve: Curve.Linear })
      .onEnter((type: RouteType, progress: number) => {
        this.scale = 1
        this.opacity = progress
      }) // 进场过程中会逐帧触发onEnter回调，入参为动效的归一化进度(0% -- 100%)
    PageTransitionExit({ duration: 1500, curve: Curve.Ease })
      .onExit((type: RouteType, progress: number) => {
        this.scale = 1 - progress
        this.opacity = 1
      }) // 退场过程中会逐帧触发onExit回调，入参为动效的归一化进度(0% -- 100%)
  }
}