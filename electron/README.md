# most

> MOST

#### Build Setup

``` bash
# install dependencies
npm install

# serve with hot reload at localhost:9080
npm run dev

# build electron application for production
npm run build


# lint all JS/Vue component files in `src/`
npm run lint

```

vue-popper
  修改package.json，指向vue-popper/dist/vue-popper.js文件，修改此文件mounted函数switch前增加
      if (typeof this.forceShow === 'boolean') return;
---

vxe-table
  lib\table\src\methods.js
    在1697行
      } else if (isEnter && (keyboardConfig.isArrow || keyboardConfig.isTab) && (selected.row || actived.row || treeConfig && highlightCurrentRow && currentRow)) {
    后面1698行添加
      (selected.row ? selected.args : actived.args).enterToColumnIndex = keyboardConfig.enterToColumnIndex
  lib\keyboard\src\mixin.js
    在161行
      } else if (isDwArrow && params.rowIndex < afterFullData.length - 1) {
    后面162行添加
      // 回车到指定列
      if (params.enterToColumnIndex) {
        params.columnIndex = params.enterToColumnIndex;
        params.column = visibleColumn[params.enterToColumnIndex];
      }

  lib\edit\src\mixin.js
    在598行添加
      _this7.$emit('selected-changed', { row, column });

This project was generated with [electron-vue](https://github.com/SimulatedGREG/electron-vue)@[45a3e22](https://github.com/SimulatedGREG/electron-vue/tree/45a3e224e7bb8fc71909021ccfdcfec0f461f634) using [vue-cli](https://github.com/vuejs/vue-cli). Documentation about the original structure can be found [here](https://simulatedgreg.gitbooks.io/electron-vue/content/index.html).
