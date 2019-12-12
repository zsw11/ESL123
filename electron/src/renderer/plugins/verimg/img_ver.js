function imgVer(Config) {
// 图形滑块验证码参数配置
    var el = eval(Config.el);
    var w = Config.width;
    var h = Config.height;
    var imgLibrary = Config.img;

// 设置拼图缺失坐标取值范围
    var PL_Size = 48;//**缺失拼图的大小
    var padding = 20;//缺失拼图与边框的距离
    var MinN_X = padding + PL_Size;
    var MaxN_X = w - padding - PL_Size - PL_Size/6;
    var MaxN_Y = padding;
    var MinN_Y = h - padding - PL_Size - PL_Size/6;

    //生成随机数的公共函数
    function RandomNum(Min, Max) {
        var Range = Max - Min;
        var Rand = Math.random();
        if(Math.round(Rand * Range)==0){
            return Min + 1;
        }else if(Math.round(Rand * Max)==Max) {
            // index++;
            return Max - 1;
        }else {
            var num = Min + Math.round(Rand * Range) - 1;
            return num;
        }
    }
//随机读取图片库的图片
    var imgRandom = RandomNum(1,imgLibrary.length);//从图库随机抽取
    var imgSrc = imgLibrary[imgRandom];

//生成缺失拼图的随机坐标 X,Y
    var X = RandomNum(MinN_X, MaxN_X);
    var Y = RandomNum(MinN_Y, MaxN_Y);
// 拼图初始化存在的left位置
    var left_Num = -X + 10;
// 图形滑块验证码 DOM 生成
    var html = '<div style="position:relative;padding:16px 16px 28px;border:1px solid #ddd;background:#f2ece1;border-radius:16px;">';
    html += '<div style="position:relative;overflow:hidden;width:'+ w +'px;">';
    html += '<div style="position:relative;width:'+ w + 'px;height:'+ h +'px;">';
    html += '<img id="scream" src="'+ imgSrc +'" style="width:'+ w +'px;height:' + h + 'px;">';
    html += '<canvas id="puzzleBox" width="'+ w +'" height="'+ h +'" style="position:absolute;left:0;top:0;z-index:22;"></canvas>';
    html += '</div>';
    html += '<div class="puzzle-lost-box" style="position:absolute;width:'+ w + 'px;height:'+ h +'px;top:0;left:'+ left_Num +'px;z-index:111;">';
    html += '<canvas id="puzzleShadow" width="'+ w +'" height="'+ h +'" style="position:absolute;left:0;top:0;z-index:22;"></canvas>';
    html += '<canvas id="puzzleLost" width="'+ w +'" height="'+ h +'" style="position:absolute;left:0;top:0;z-index:33;"></canvas>';
    html += '</div>';
    html += '<p class="ver-tips"></p>';
    html += '</div>';
    html += '<div class="re-btn"><a></a></div>';
    html += '</div>';
    html += '<br>';
    html += '<div style="position:relative;width:'+ w +'px;margin:auto;">';
    html += '<div style="border:1px solid #c3c3c3;border-radius:24px;background:#ece4dd;box-shadow:0 1px 1px rgba(12,10,10,0.2) inset;">';
    html += '<p style="font-size:12px;color: #486c80;line-height:28px;margin:0;text-align:right;padding-right:22px;">按住左边滑块，拖动完成上方拼图</p>';
    html += '</div>';
    html += '<div class="slider-btn"></div>';
    html += '</div>';
    el.html(html);

// 随机生成缺失拼图

    //使用canvas画出缺失拼图
    var d = PL_Size/3;
    var c=document.getElementById("puzzleBox");
    var ctx = c.getContext("2d");

    ctx.globalCompositeOperation ="xor";
    ctx.shadowBlur = 10;
    ctx.shadowColor = "#fff";
    ctx.shadowOffsetX = 3;
    ctx.shadowOffsetY = 3;
    ctx.fillStyle = "rgba(0,0,0,0.7)";

    ctx.beginPath();
    ctx.lineWidth = "1";
    ctx.strokeStyle = "rgba(0,0,0,0)";
    ctx.moveTo(X,Y);
    ctx.lineTo(X+d,Y);
    ctx.bezierCurveTo(X+d,Y-d,X+2*d,Y-d,X+2*d,Y);
    ctx.lineTo(X+3*d,Y);
    ctx.lineTo(X+3*d,Y+d);
    ctx.bezierCurveTo(X+2*d,Y+d,X+2*d,Y+2*d,X+3*d,Y+2*d);
    ctx.lineTo(X+3*d,Y+3*d);
    ctx.lineTo(X,Y+3*d);
    ctx.closePath();
    ctx.stroke();
    ctx.fill();

    //生成缺失 的 拼图并且凸显
    var c_l=document.getElementById("puzzleLost");
    var c_s=document.getElementById("puzzleShadow");
    var ctx_l = c_l.getContext("2d");
    var ctx_s = c_s.getContext("2d");
    var img = new Image();
    img.src = imgSrc;
    img.onload = function() {
        ctx_l.drawImage(img,0,0,w,h);
    }

    ctx_l.beginPath();
    // ctx_l.lineWidth = "5";
    // ctx_l.strokeStyle = "#ecf093";
    ctx_l.strokeStyle = "rgba(0,0,0,0)";
    ctx_l.moveTo(X,Y);
    ctx_l.lineTo(X+d,Y);
    ctx_l.bezierCurveTo(X+d,Y-d,X+2*d,Y-d,X+2*d,Y);
    ctx_l.lineTo(X+3*d,Y);
    ctx_l.lineTo(X+3*d,Y+d);
    ctx_l.bezierCurveTo(X+2*d,Y+d,X+2*d,Y+2*d,X+3*d,Y+2*d);
    ctx_l.lineTo(X+3*d,Y+3*d);
    ctx_l.lineTo(X,Y+3*d);
    ctx_l.closePath();
    ctx_l.stroke();
    ctx_l.shadowBlur = 10;
    ctx_l.shadowColor = "black";
    ctx_l.clip();

    ctx_s.beginPath();
    ctx_s.lineWidth = "1";
    ctx_s.strokeStyle = "rgba(0,0,0,0)";
    ctx_s.moveTo(X,Y);
    ctx_s.lineTo(X+d,Y);
    ctx_s.bezierCurveTo(X+d,Y-d,X+2*d,Y-d,X+2*d,Y);
    ctx_s.lineTo(X+3*d,Y);
    ctx_s.lineTo(X+3*d,Y+d);
    ctx_s.bezierCurveTo(X+2*d,Y+d,X+2*d,Y+2*d,X+3*d,Y+2*d);
    ctx_s.lineTo(X+3*d,Y+3*d);
    ctx_s.lineTo(X,Y+3*d);
    ctx_s.closePath();
    ctx_s.stroke();
    ctx_s.shadowBlur = 20;
    ctx_s.shadowColor = "black";
    ctx_s.fill();

// 滑块拖动
    var moveStart = '';//定义一个鼠标按下的X轴值
    //鼠标按下
    $(".slider-btn").mousedown(function (e) {
        e = e || window.event;
        // 鼠标在滑块按下切换滑块背景
        $(this).css({
            "background-position":"0 -216px"
        });
        moveStart = e.pageX;//记录鼠标按下时的坐标 X轴值
    });
    //鼠标拖动（这里使用全局监听鼠标移动的事件）
    onmousemove = function(e) {
        e = e || window.event;
        var moveX = e.pageX;//监听鼠标的位置
        var d = moveX-moveStart;//鼠标按住后在X轴上移动的距离
        if(moveStart == '') {
            // console.log('未拖动滑块');
        } else {
            if(d < 0 || d > (w-padding-PL_Size)) {
                // console.log('超过范围');
            } else {
                $(".slider-btn").css({
                    "left":d + 'px',
                    "transition":"inherit"
                });
                $("#puzzleLost").css({
                    "left":d + 'px',
                    "transition":"inherit"
                });
                $("#puzzleShadow").css({
                    "left":d + 'px',
                    "transition":"inherit"
                });
            }
        }
    };

    //鼠标松开 （这里使用全局监听鼠标松开的事件）
    onmouseup = function (e) {
        e = e || window.event;
        var moveEnd_X = e.pageX - moveStart;//松开鼠标后滑块移动的距离
        var ver_Num = X-10;
        var deviation = 4; //偏差值
        var Min_left = ver_Num - deviation;//验证值最小值
        var Max_left = ver_Num + deviation;//验证值最大值
        if(moveStart == '') {

        } else {
            if(Max_left > moveEnd_X && moveEnd_X > Min_left) {//成功执行
                $(".ver-tips").html('<i style="background-position:-4px -1207px;"></i><span style="color:#42ca6b;">验证通过</span><span></span>');
                $(".ver-tips").addClass("slider-tips");
                $(".puzzle-lost-box").addClass("hidden");
                $("#puzzleBox").addClass("hidden");
                setTimeout(function () {
                    $(".ver-tips").removeClass("slider-tips");
                    imgVer(Config);
                },2000);
                Config.success();

            } else {//失败执行
                $(".ver-tips").html('<i style="background-position:-4px -1229px;"></i><span style="color:red;">验证失败:</span><span style="margin-left:4px;">拖动滑块将悬浮图像正确拼合</span>');
                $(".ver-tips").addClass("slider-tips");
                setTimeout(function () {
                    $(".ver-tips").removeClass("slider-tips");
                },2000);
                Config.error();
            }
        }
        setTimeout(function () {
            $(".slider-btn").css({
                "left":'0',
                "transition":"left 0.5s"
            });
            $("#puzzleLost").css({
                "left":'0',
                "transition":"left 0.5s"
            });
            $("#puzzleShadow").css({
                "left":'0',
                "transition":"left 0.5s"
            });
        },1000);
        $(".slider-btn").css({
            "background-position":"0 -84px"
        });
        moveStart = '';//  清空上一次鼠标按下时的坐标X轴值;
        $(".re-btn a").on("click",function () {
            imgVer(Config);
        })
    }
}