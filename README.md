## 安装

```shell script
vd plugin install vd/lottie
```

## 卸载

```shell script
vd plugin uninstall vd/lottie
```


## 配置参数

| 属性名           | 类型     | 描述                          | 默认值     |
| ------------- | ------ | -------------------------- | ------- |
| src |`String`  | 资源地址           | -       |
| loop |`Boolean`  | 循环播放           | true       |
| speed |`Number`  | 设置动画的速度。接受反向动画的负值           | 1       |
| content |`JSONObject`  | 资源内容，与`src`二选一           | -       |


## 事件回调 `callback`

``` js
/**
 * 组件加载完成
 */
@ready = function() { ... }

/**
 * 监听状态发生改变
 * 返回参数：data = {
                    status:'success',       //状态，注①
                    src: 'http://....',
                }
 */
@stateChanged = function(data) { ... }
```

> 注①：

- `loading`加载src加载中
- `success`加载成功
- `error`加载失败

## 调用方法 `methods`

```js
//播放
this.$refs.reflectName.play();

//暂停播放
this.$refs.reflectName.pause();

//恢复播放
this.$refs.reflectName.resume();

//停止播放
this.$refs.reflectName.stop();

//设置src
this.$refs.reflectName.setSrc(string);

//设置Loop
this.$refs.reflectName.setLoop(Boolean);

//设置Speed
this.$refs.reflectName.setSpeed(Number);

//设置Content
this.$refs.reflectName.setContent(string|JSONObject);

```

## 示例代码

[https://editor.vd.app/#/files/vd/demo/src/pages/lottie/sample.vue](https://editor.vd.app/#/files/vd/demo/src/pages/lottie/sample.vue)"