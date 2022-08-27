
function getTime() {
  var data = new Date();

  var year = data.getFullYear(),
    month = data.getMonth() + 1,
    date = data.getDate(),
    day = data.getDay(),
    week = [
      "星期日",
      "星期一",
      "星期二",
      "星期三",
      "星期四",
      "星期五",
      "星期六",
    ],
    h = data.getHours(),
    m = data.getMinutes(),
    s = data.getSeconds(),
    h = checkTime(h),
    m = checkTime(m),
    s = checkTime(s);

  return (
    year + "年" + month + "月" + date + "日" + week[day] + " " + h + ":" + m
  );
};

var checkTime = function (i) {
  if (i < 10) {
    i = "0" + i;
  }

  return i;
};
