'use strict';

const _ = require('lodash');
const fs = require('fs');
const path = require('path');
const jsonfile = require('jsonfile');
const handlebars = require('handlebars');
const inquirer = require('inquirer');
const chalk = require('chalk');
const symbols = require('log-symbols');
const app = require('./mock-app');

function mkdirsSync(dirname) {
  if (fs.existsSync(dirname)) {
    return true;
  } else {
    if (mkdirsSync(path.dirname(dirname))) {
      fs.mkdirSync(dirname);
      return true;
    }
  }
}

function checkVue() {
  return new Promise((resolve, reject) => {
    jsonfile.readFile('./package.json', function(err, obj) {
      if (err) {
        if (err.code === 'ENOENT') {
          console.log('找不到package.json文件');
        } else if (err instanceof SyntaxError) {
          console.log('package.json文件格式错误');
        } else {
          console.log(err);
        }
        reject();;
      }
      if (!obj.dependencies.vue || !obj.dependencies['element-ui']) {
        console.log('项目不是vue的admin项目');
        reject();
      }

      resolve();
    });
  })
}

// 读取模型文件
function readModelFile(path) {
  if (!fs.existsSync(path)) return console.log(symbols.error, chalk.red('模型文件不存在'));
  // 读取模型信息
  let model, modelName, modelCnName, paranoid;
  try {
    model = require(path)(app);
    modelName = model.options && model.options.tableName ? model.options.tableName : model.tableName;
    if (!modelName || !model.attrs) throw new Error('缺少模型信息');
    modelCnName = model.options && model.options.tableCnName ? model.options.tableCnName : '';
    paranoid = model.options && model.options.paranoid === false ? false : true;
  } catch(err) {
    console.log(symbols.error, chalk.red('读取模型文件出错'));
    return;
  }
  return { model, modelName, modelCnName, paranoid };
}

// 读取模型文件并转为meta
function metaFromModelFile(path, options) {
  return new Promise((resolve) => {
    // -- 根据模型信息生成
    const { model, modelName, modelCnName, paranoid } = readModelFile(path);

    function promptAttrs(){
      const attrs = options && options.promptAttrsOmit ? _.omit(model.attrs, options.promptAttrsOmit) : model.attrs;
      return inquirer.prompt([{
        type: 'checkbox',
        name: 'selectedAttrs',
        message: '请选择需要处理的属性',
        choices: _.map(attrs, (attr, k) => { return {
          name: `${k} (${attr.cnName})`,
          value: k,
          checked: true,
        }; }).concat(options.promptAttrsWithTimestamps ? [
          { name: 'createdAt (创建时间)', value: 'createdAt', checked: true },
          { name: 'updatedAt (修改时间)', value: 'updatedAt', checked: true }
        ] : []),
        pageSize: 20,
      }]);
    }
    // 有标注中文名
    if (modelCnName) {
      const meta = {
        modelName,
        attrs: model.attrs,
        modelCnName,
        paranoid,
      };
      // 设置选择字段
      if (options && options.promptAttrs) {
        promptAttrs().then(answers => {
          meta.selectedAttrs = answers.selectedAttrs;
          resolve(meta);
        });
      } else {
        resolve(meta);
      }
    } else {
      // 用户输入中文名
      inquirer.prompt([{
        type: 'input',
        name: 'modelCnName',
        message: '请输入模型中文名称'
      }]).then((answers) => {
        const meta = {
          modelName,
          attrs: model.attrs,
          modelCnName: answers.modelCnName,
          paranoid,
        };
        // 设置选择字段
        if (options && options.promptAttrs) {
          promptAttrs().then(answers => {
            meta.selectedAttrs = answers.selectedAttrs;
            resolve(meta);
          });
        } else {
          resolve(meta);
        }
      })
    }
  })
}

// 可用UI控件类型
const UITypes = [
  { name: '输入框', value: 'input' },
  { name: '计数器', value: 'inputNumber' },
  new inquirer.Separator(),
  { name: '单选框', value: 'radio' },
  // { name: '多选框', value: 'checkBox' },
  { name: '选择器', value: 'select' },
  { name: '字典表选择', value: 'remoteDict' },
  { name: '搜索选择', value: 'remoteSelect' },
  new inquirer.Separator(),
  { name: '开关', value: 'switch' },
  { name: '滑块', value: 'slider' },
  new inquirer.Separator(),
  { name: '日期选择器', value: 'datePicker' },
  { name: '时间选择器', value: 'timePicker' },
  { name: '日期时间选择器', value: 'datetimePicker' },
];
function getUITypeName(id) {
  return _.find(UITypes, { value: id }).name;
}
// 根据数据库字段类型生成
function defaultUIType(attr) {
  switch(attr.type.typeId) {
    case 'integer':
    case 'decimal':
      return 'inputNumber';
    case 'boolean':
      return 'switch';
    case 'date':
      return 'datetimePicker';
    case 'dateOnly':
      return 'datePicker';
    default:
      return 'input';
  }
  return ;
}
// 修改属性UI控件
function promptChangeUI(attrs) {
  return new Promise(resolve => {
    (function p() {
      const choices = [{ name: '不需调整', value: '__noNeedToChange__' }];
      _.forEach(attrs, (v, k) => {
        if (!v.UIType) v.UIType = defaultUIType(v);
        choices.push({ name: `${k} [${v.cnName}]:${_.repeat('\t', 3 - parseInt((k.length - 1) / 8))} (${getUITypeName(v.UIType)})`, value: k });
      });
      inquirer.prompt([{
        type: 'list',
        name: 'attrName',
        message: '如需调整UI控件，请选择：',
        choices,
        pageSize: 20,
      }]).then(answers => {
        if (answers.attrName === '__noNeedToChange__') {
          return resolve(_.uniq(Object.values(attrs).map(attr => attr.UIType)));
        }
        else {
          inquirer.prompt([{
            type: 'list',
            name: 'UIType',
            message: '请选择UI类型',
            choices: UITypes,
            default: attrs[answers.attrName].UIType,
            pageSize: 20,
          }]).then(UIAnswers => {
            attrs[answers.attrName].UIType = UIAnswers.UIType;
            p();
          });
        }
      })
    })();
  })
}

function promptModel(extra = []) {
  return inquirer.prompt([{
    type: 'input',
    name: 'moduleName',
    message: '请输入模块名称',
  }, {
    type: 'input',
    name: 'modelName',
    message: '请输入模型名称',
  }, {
    type: 'input',
    name: 'modelCnName',
    message: '请输入模型中文名称',
  }].concat(extra));
}

function promptResource(extra = []) {
  return inquirer.prompt([{
    type: 'input',
    name: 'resourceId',
    message: '请输入资源名称'
  }, {
    type: 'input',
    name: 'resourceName',
    message: '请输入资源中文名称'
  }].concat(extra));
}

function promptOperates(extra = []) {
  return inquirer.prompt([{
    type: 'checkbox',
    name: 'operates',
    message: '请选择包含的操作类型',
    choices: [
      { name: '创建', value: 'create', checked: true },
      { name: '普通搜索', value: 'conditionSearch', checked: true },
      { name: '高级搜索', value: 'advancedSearch', checked: false },
      { name: '字段显示', value: 'showField', checked: false },
      { name: '修改', value: 'update', checked: true },
      { name: '删除', value: 'delete', checked: true },
      { name: '导入', value: 'import', checked: false },
      { name: '导出', value: 'export', checked: false },
      new inquirer.Separator(),
      { name: '刷新', value: 'refresh', checked: true },
    ],
    pageSize: 20,
  }].concat(extra));
}

function compile(options) {
  const { targetPath, targetFile, templatePath, meta } = options;
  mkdirsSync(targetPath);
  const content = fs.readFileSync(templatePath).toString();
  const result = handlebars.compile(content)(meta);
  fs.writeFileSync(`${targetPath}/${targetFile}`, result);
}

function chooseAttrs(path) {
  const { model } = readModelFile(path);
  const attrs = model.attrs;
  return inquirer.prompt([{
    type: 'checkbox',
    name: 'chooseAttrs',
    message: '请选择需要处理的搜索属性',
    choices: _.map(attrs, (attr, k) => { return {
      name: `${k} (${attr.cnName})`,
      value: k,
      checked: true,
    }; }),
    pageSize: 20,
  }]);
}

function orderAttrs(path) {
  const { model } = readModelFile(path);
  const attrs = model.attrs;
  return inquirer.prompt([{
    type: 'checkbox',
    name: 'orderAttrs',
    message: '请选择需要排序属性',
    choices: _.map(attrs, (attr, k) => { return {
      name: `${k} (${attr.cnName})`,
      value: k,
      checked: false,
    }; }),
    pageSize: 20,
  }]);
}

module.exports = {
  mkdirsSync,
  checkVue,
  metaFromModelFile,
  promptResource,
  promptModel,
  promptOperates,
  promptChangeUI,
  compile,
  chooseAttrs,
  orderAttrs,
};
