// @ts-nocheck
@Entry
@Component
struct VideoExample {
  @State src: Resource = $rawfile('video1.mp4')
  @State currentProgressRate: number = 1
  @State muted: boolean = false
  @State autoPlay: boolean = false
  @State controls: boolean = true
  @State startStaus: boolean = true
  myVideoController: VideoController = new VideoController()

  build() {
    Column({ space: 10 }) {
      Video({
        src: this.src,
        previewUri: '../../../../resources/rawfile/video1.mp4',
        currentProgressRate: this.currentProgressRate,
        controller: this.myVideoController
      })
        .muted(this.muted)
        .autoPlay(this.autoPlay)
        .controls(this.controls)
        .objectFit(ImageFit.Contain)
        .loop(true)
        .width(320)
        .height(200)
        .onStart(() => {
          console.info('onStart')
        })
        .onPause(() => {
          console.info('onPause')
        })
        .onFinish(() => {
          console.info('onFinish')
        })
        .onError(() => {
          console.info('onError')
        })
        .onFullscreenChange((e) => {
          console.info('onFullscreenChange:' + e.fullscreen)
        })
        .onPrepared((e) => {
          console.info('onPrepared:' + e.duration)
        })
        .onSeeking((e) => {
          console.info('onSeeking' + e.time)
        })
        .onSeeked((e) => {
          console.info('onSeeked' + e.time)
        })
        .onUpdate((e) => {
          console.info('onUpdate' + e.time)
        })

      Row() {
        Flex({ wrap: FlexWrap.Wrap, justifyContent: FlexAlign.SpaceAround, alignItems:ItemAlign.Start }) {
          Button("src")
            .onClick(() => {
              if (this.src == $rawfile('video1.mp4')) {
                this.src = $rawfile('video2.mp4')
              } else {
                this.src = $rawfile('video1.mp4')
              }
            })
            .margin({bottom:10})
          Button("controls")
            .onClick(() => {
              this.controls = !this.controls
            })
          Button("play")
            .onClick(() => {
              this.myVideoController.start()
            })
          Button("pause")
            .onClick(() => {
              this.myVideoController.pause()
            })
          Button("stop")
            .onClick(() => {
              this.myVideoController.stop()
            })
            .margin({bottom:10})
          Button("requestFullscreen")
            .onClick(() => {
              this.myVideoController.requestFullscreen(true)
            })
          Button("exitFullscreen")
            .onClick(() => {
              this.myVideoController.exitFullscreen()
            })
          Button("setCurrentTime")
            .onClick(() => {
              this.myVideoController.setCurrentTime(9)
            })
        }.padding(15)
      }
    }.width('100%')
  }
}