#!/usr/bin/env node
'use strict';

const _ = require('lodash');
const program = require('commander');
const inquirer = require('inquirer');
const handlebars = require('handlebars');
const fs = require('fs');
const chalk = require('chalk');
const symbols = require('log-symbols');
const jsonfile = require('jsonfile');
const download = require('download-git-repo');

const { metaFromModelFile, checkVue, compile, promptModel,
  promptResource, promptOperates, promptChangeUI, chooseAttrs, orderAttrs } = require('./lib/util');
require('./lib/helper')(handlebars);

const cwd = process.cwd();
program.version('0.1.0', '-v, --version');

program
  .command('init <name> <tpl>')
  .action((name, tpl) => {
    if (!/^\w+$/.test(name)) {
      console.log('请输入正确的项目名称。');
      return;
    }
    download(`gitee.com:apjcorp/admin-app#${tpl}`, name, { clone: true }, err => {
      if (err) {
        console.log('下载项目模板出错，请检查目录是否已经存在，模板名称是否正确！');
        return;
      }
      inquirer.prompt([
        {
          name: 'description',
          message: '请输入项目描述',
          default: '项目前端',
        },
        {
          name: 'author',
          message: '请输入作者名称',
          default: 'APJ PSD',
        },
      ]).then(answer => {
        const meta = Object.assign(answer, {
          projectName: name,
        });
        const compileFiles = jsonfile.readFileSync(`${name}/.templaterc`).files.map(f => name + '/' + f);
        for (const f of compileFiles) {
          const content = fs.readFileSync(f, { encoding: 'utf8' });
          const result = handlebars.compile(content)(meta);
          fs.writeFileSync(f, result);
        }
        fs.unlinkSync(`${name}/.templaterc`);
      });
    });
  });

// 增加api文件
program
  .command('addapi <apiFile>')
  .option('-m, --modelFile <modelFile>', '模型文件')
  .action((apiFile, cmd) => {
    checkVue().then(() => {
      apiFile = apiFile.replace(/\.js$/i, '');
      if (cmd.modelFile) {
        metaFromModelFile(cmd.modelFile).then(meta => {
          inquirer.prompt([
            {
              type: 'input',
              name: 'module',
              message: 'API前缀',
            }, {
              type: 'checkbox',
              name: 'operates',
              message: '请选择包含的操作类型',
              choices: [
                { name: '创建', value: 'create', checked: true },
                { name: '高级搜索', value: 'advancedSearch', checked: false },
                { name: '字段显示', value: 'showField', checked: false },
                { name: '修改', value: 'update', checked: true },
                { name: '删除', value: 'delete', checked: true },
                { name: '导入', value: 'import', checked: false },
                { name: '导出', value: 'export', checked: false },
              ],
            },
          ]).then(answers => {

            compile({
              targetPath: `${cwd}/src/api`,
              targetFile: `${apiFile}.js`,
              templatePath: `${__dirname}/templates/api.model.js.hbs`,
              meta: Object.assign(meta, answers) });
            console.log(symbols.success, chalk.green('增加模型api文件成功'));
          });
        });
      } else {
        promptResource([
          {
            type: 'input',
            name: 'module',
            message: 'API前缀',
          }, {
            type: 'checkbox',
            name: 'operates',
            message: '请选择包含的操作类型',
            choices: [
              { name: '创建', value: 'create', checked: true },
              { name: '高级搜索', value: 'advancedSearch', checked: false },
              { name: '字段显示', value: 'showField', checked: false },
              { name: '修改', value: 'update', checked: true },
              { name: '删除', value: 'delete', checked: true },
              { name: '导入', value: 'import', checked: false },
              { name: '导出', value: 'export', checked: false },
            ],
          },
        ]).then(answers => {
          compile({
            targetPath: `${cwd}/src/api`,
            targetFile: `${apiFile}.js`,
            templatePath: `${__dirname}/templates/api.resource.js`,
            meta: answers,
          });
          console.log(symbols.success, chalk.green('增加资源api文件成功'));
        });
      }
    });
  });

// 增加页面
program
  .command('addpage <type> <pageName>')
  .option('-m, --modelFile <modelFile>', '模型文件')
  .action((type, pageName, cmd) => {
    const match = /^(\w+(?:\\|\/))?((?:\w|-)+)(?:\.vue)?$/.exec(pageName);
    if (!match) return console.log(symbols.error, chalk.red('文件名不合法，最多支持两级'));
    const [ m, p, f ] = match;
    // 读取json文件判断是否vue admin项目
    checkVue().then(() => {
      switch (type) {

        // list页面样例
        case 'listexample': {
          compile({
            targetPath: `${cwd}/src/views/${p}`,
            targetFile: `${f}.vue`,
            templatePath: `${__dirname}/templates/list.example.vue`,
            meta: {},
          });
          console.log(symbols.success, chalk.green('增加list页面样例成功'));
          break;
        }

        // 列表页面
        case 'list': {
          if (cmd.modelFile) {
            // 读取模型信息并生成meta
            metaFromModelFile(cmd.modelFile, {
              promptAttrs: true,
              promptAttrsWithTimestamps: true,
            }).then(meta => {
              promptOperates([
                {
                  type: 'input',
                  name: 'module',
                  message: '输入模块名称',
                },
              ]).then(answers => {
                meta.operates = answers.operates;
                meta.module = answers.module;
                orderAttrs(cmd.modelFile).then(({ orderAttrs }) => {
                  meta.orderAttrs = orderAttrs;
                  if (meta.operates.includes('conditionSearch')) {
                    chooseAttrs(cmd.modelFile).then(({ chooseAttrs }) => {
                      meta.chooseAttrs = chooseAttrs;
                      promptChangeUI(_.pick(meta.attrs, chooseAttrs)).then(UITypes => {
                        meta.UITypes = UITypes;
                        compile({
                          targetPath: `${cwd}/src/views/modules/${answers.module}`,
                          targetFile: `${f.toLowerCase()}.vue`,
                          templatePath: `${__dirname}/templates/list.model.vue.hbs`,
                          meta,
                        });
                        console.log(symbols.success, chalk.green('增加list页面成功'));
                      });
                    });
                  } else {
                    compile({
                      targetPath: `${cwd}/src/views/modules/${answers.module}`,
                      targetFile: `${f.toLowerCase()}.vue`,
                      templatePath: `${__dirname}/templates/list.model.vue.hbs`,
                      meta,
                    });
                    console.log(symbols.success, chalk.green('增加list页面成功'));
                  }
                });
              });
            });
          } else {
            // -- 资源列表，没有属性信息
            promptResource().then(answers => {
              promptOperates([
                {
                  type: 'input',
                  name: 'module',
                  message: '输入模块名称',
                },
              ]).then(operates => {
                answers.module = operates.module;
                compile({
                  targetPath: `${cwd}/src/views/${answers.module}`,
                  targetFile: `${f.toLowerCase()}.vue`,
                  templatePath: `${__dirname}/templates/list.resource.vue`,
                  meta: answers,
                });
                console.log(symbols.success, chalk.green('增加list页面成功'));
              });
            });
          }
          break;
        }

        // edit页面样例
        case 'updateexample':
        case 'editexample': {
          compile({
            targetPath: `${cwd}/src/views/${p}`,
            targetFile: `${f}.vue`,
            templatePath: `${__dirname}/templates/edit.example.vue`,
            meta: {},
          });
          console.log(symbols.success, chalk.green('增加edit页面样例成功'));
          break;
        }

        // edit页面
        case 'update':
        case 'edit': {
          if (cmd.modelFile) {
            // 读取模型信息并生成meta
            metaFromModelFile(cmd.modelFile, {
              promptAttrs: true,
              promptAttrsOmit: [ 'id', 'status', 'createdAt', 'updatedAt' ],
            }).then(meta => {
              promptChangeUI(_.pick(meta.attrs, meta.selectedAttrs)).then(UITypes => {
                meta.UITypes = UITypes;
                compile({
                  targetPath: `${cwd}/src/views/${p}`,
                  targetFile: `${f}.vue`,
                  templatePath: `${__dirname}/templates/edit.model.vue`,
                  meta,
                });
                console.log(symbols.success, chalk.green('增加edit页面成功'));
              });
            });
          } else {
            // -- 资源列表，没有属性信息
            promptResource().then(answers => {
              compile({
                targetPath: `${cwd}/src/views/${p}`,
                targetFile: `${f}.vue`,
                templatePath: `${__dirname}/templates/edit.resource.vue`,
                meta: answers,
              });
              console.log(symbols.success, chalk.green('增加edit页面成功'));
            });
          }
          break;
        }

        // new页面样例
        case 'createexample':
        case 'newexample': {
          compile({
            targetPath: `${cwd}/src/views/${p}`,
            targetFile: `${f}.vue`,
            templatePath: `${__dirname}/templates/new.example.vue`,
            meta: {},
          });
          console.log(symbols.success, chalk.green('增加new页面样例成功'));
          break;
        }

        // new页面
        case 'create':
        case 'new': {
          if (cmd.modelFile) {
            // 读取模型信息并生成meta
            metaFromModelFile(cmd.modelFile, {
              promptAttrs: true,
              promptAttrsOmit: [ 'id', 'status', 'createdAt', 'updatedAt' ],
            }).then(meta => {
              promptChangeUI(_.pick(meta.attrs, meta.selectedAttrs)).then(UITypes => {
                meta.UITypes = UITypes;
                compile({
                  targetPath: `${cwd}/src/views/${p}`,
                  targetFile: `${f}.vue`,
                  templatePath: `${__dirname}/templates/new.model.vue`,
                  meta,
                });
                console.log(symbols.success, chalk.green('增加new页面成功'));
              });
            });
          } else {
            // -- 资源列表，没有属性信息
            promptResource().then(answers => {
              compile({
                targetPath: `${cwd}/src/views/${p}`,
                targetFile: `${f}.vue`,
                templatePath: `${__dirname}/templates/new.resource.vue`,
                meta: answers,
              });
              console.log(symbols.success, chalk.green('增加new页面成功'));
            });
          }
          break;
        }

        // 维护页面
        case 'maintain': {
          if (cmd.modelFile) {
            // 读取模型信息并生成meta
            metaFromModelFile(cmd.modelFile, {
              promptAttrs: true,
              promptAttrsOmit: [ 'id', 'status', 'createdAt', 'updatedAt' ],
            }).then(meta => {
              promptChangeUI(_.pick(meta.attrs, meta.selectedAttrs)).then(UITypes => {
                meta.UITypes = UITypes;
                inquirer.prompt([
                  {
                    type: 'input',
                    name: 'module',
                    message: '输入模块名称',
                  },
                ]).then(operates => {
                  meta.module = operates.module;
                  compile({
                    targetPath: `${cwd}/src/views/modules/${operates.module}`,
                    targetFile: `${f.toLowerCase()}-maintain.vue`,
                    templatePath: `${__dirname}/templates/maintain.model.vue.hbs`,
                    meta,
                  });
                  console.log(symbols.success, chalk.green('增加maintain页面成功'));
                });
              });
            });
          } else {
            // -- 资源列表，没有属性信息
            promptResource().then(answers => {
              inquirer.prompt([
                {
                  type: 'input',
                  name: 'module',
                  message: '输入模块名称',
                },
              ]).then(operates => {
                answers.module = operates.module;
                compile({
                  targetPath: `${cwd}/src/views/${operates.module}`,
                  targetFile: `${f.toLowerCase()}-maintain.vue`,
                  templatePath: `${__dirname}/templates/new.resource.vue`,
                  meta: answers,
                });
                console.log(symbols.success, chalk.green('增加new页面成功'));
              });
            });
          }
          break;
        }

        default: {
          console.log(symbols.error, chalk.red('页面类型错误'));
        }
      }
    }, function() {
      return;
    });
  });


// 增加路由文件
program
  .command('addroute')
  .action(() => {
    // 读取json文件判断是否egg项目
    checkVue().then(() => {
      promptModel().then(answers => {
        const content = fs.readFileSync(`${__dirname}/templates/route.base.hbs`).toString();
        const result = handlebars.compile(content)(answers);
        console.log(result);
      });

    });
  });


program.parse(process.argv);
