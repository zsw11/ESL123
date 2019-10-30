'use strict';

const _ = require('lodash');

function upperFirst(item) {
  return _.upperFirst(item);
}

function toLower(item) {
  return _.toLower(item);
}

function kebabCase(item) {
  return _.kebabCase(item);
}

function rmId(item) {
  return item.replace(/Id$/, '');
}

function upperFirstRmId(item) {
  return _.upperFirst(item).replace(/Id$/, '');
}

function compare(v1, operator, v2, options) {
  switch (operator) {
    case '==':
      return v1 === v2 ? options.fn(this) : options.inverse(this);
    case '===':
      return v1 === v2 ? options.fn(this) : options.inverse(this);
    case '!=':
      return v1 !== v2 ? options.fn(this) : options.inverse(this);
    case '!==':
      return v1 !== v2 ? options.fn(this) : options.inverse(this);
    case '<':
      return v1 < v2 ? options.fn(this) : options.inverse(this);
    case '<=':
      return v1 <= v2 ? options.fn(this) : options.inverse(this);
    case '>':
      return v1 > v2 ? options.fn(this) : options.inverse(this);
    case '>=':
      return v1 >= v2 ? options.fn(this) : options.inverse(this);
    case '&&':
      return v1 && v2 ? options.fn(this) : options.inverse(this);
    case '||':
      return v1 || v2 ? options.fn(this) : options.inverse(this);
    case 'test':
      return new RegExp(v1).test(v2) ? options.fn(this) : options.inverse(this);
    case 'includes':
      return v1.includes(v2) ? options.fn(this) : options.inverse(this);
    case 'in':
      return v2.includes(v1) ? options.fn(this) : options.inverse(this);
    case 'includesAny':
      if (v2) {
        v2 = JSON.parse(v2);
      }
      return _.intersection(v1, v2).length > 0 ? options.fn(this) : options.inverse(this);
    case 'length !== 0':
      return v1.length !== 0 ? options.fn(this) : options.inverse(this);
    default:
      return options.inverse(this);
  }
}

function switchFunc(item, options) {
  this.__switchType__ = item;
  return options.fn(this);
}

function caseFunc(item, options) {
  if (this.__switchType__ === item) {
    this.__switchDone__ = true;
    return options.fn(this);
  }
}

function defaultFunc(options) {
  if (!this.__switchDone__) return options.fn(this);
}

// 打印array
function printStringArray(array) {
  return `['${array.join("', '")}']`;
}

// 处理object
function forOf(items, options) {
  let out = '';
  for (const [ k, v ] of Object.entries(items)) {
    out += options.fn(Object.assign({
      k,
      v,
    }, options.hash));
  }
  return out;
}

// 判断是否存在数组中
function isExistsInArr(items, item, name, options) {
  let out = false;
  if (name === undefined) {
    for (const v of Object.values(items)) {
      if (v === item) {
        out = true;
      }
    }
  } else {
    for (const v of Object.values(items)) {
      if (v[name] === item) {
        out = true;
      }
    }
  }
  if (out) {
    return options.fn(this);
  }
  return options.inverse(this);
}

// 是否常规字段，而不是sequelize默认字段
function isGenAttr(item, options) {
  if ([ 'id', 'createdAt', 'updatedAt' ].includes(item)) return;
  return options.fn(this);
}

// 是否可以由客户端通过create和update进行维护
function canBeModifiedByClient(item, options) {
  if ([ 'id', 'status', 'createdAt', 'updatedAt' ].includes(item)) return;
  return options.fn(this);
}

function attrName(item) {
  return item.v.cnName || item.k;
}

function refsTable(item) {
  return item.v.refsTable || [];
}

function refsProperty(item) {
  return item.v.refsProperty || 'name';
}

// 生成新建页面的校验短句
function createValidate(col) {
  let clause = '';
  const t = col.v.type;
  switch (t.typeId) {
    case 'varchar': {
      clause += `          { max: ${t.precision}, message: '长度超过了${t.precision}', trigger: 'blur' },\n`;
      break;
    }
    case 'integer': {
      clause += `          { type: 'number', message: '${col.v.cnName}需为数字值' },\n`;
      break;
    }
    case 'decimal': {
      clause += `          { type: 'number', max: 1${_.repeat(0, t.precision - t.scale)}, message: '${col.v.cnName}需为${t.precision - (t.scale || 0)}位数字值' },\n`;
      break;
    }
    default: {
      break;
    }
  }
  if (clause) clause = `        ${col.k}: [\n${clause}        ],`;
  return clause;
}

// 生成更新页面的校验短句
function updateValidate(col) {
  let clause = '';
  const t = col.v.type;
  if (col.v.allowNull === false) {
    clause += `          { required: true, message: '请填写${col.v.cnName}', trigger: 'blur' },\n`;
  }
  switch (t.typeId) {
    case 'varchar': {
      clause += `          { max: ${t.precision}, message: '长度超过了${t.precision}', trigger: 'blur' },`;
      break;
    }
    case 'integer': {
      clause += `          { type: 'number', message: '${col.v.cnName}需为数字值' },`;
      break;
    }
    case 'decimal': {
      clause += `          { type: 'number', max: 1${_.repeat(0, t.precision - t.scale)}, message: '${col.v.cnName}需为${t.precision - (t.scale || 0)}位数字值' },`;
      break;
    }
    default: {
      break;
    }
  }
  clause = clause.replace(/,$/, '');
  if (clause) clause = `        ${col.k}: [\n${clause}\n        ],`;
  return clause;
}

module.exports = handlebars => {
  handlebars.registerHelper('upperFirst', upperFirst);
  handlebars.registerHelper('toLower', toLower);
  handlebars.registerHelper('kebabCase', kebabCase);
  handlebars.registerHelper('upperFirstRmId', upperFirstRmId);
  handlebars.registerHelper('rmId', rmId);
  handlebars.registerHelper('compare', compare);
  handlebars.registerHelper('switch', switchFunc);
  handlebars.registerHelper('case', caseFunc);
  handlebars.registerHelper('default', defaultFunc);
  handlebars.registerHelper('printStringArray', printStringArray);
  handlebars.registerHelper('forOf', forOf);
  handlebars.registerHelper('isGenAttr', isGenAttr);
  handlebars.registerHelper('canBeModifiedByClient', canBeModifiedByClient);
  handlebars.registerHelper('attrName', attrName);
  handlebars.registerHelper('refsTable', refsTable);
  handlebars.registerHelper('refsProperty', refsProperty);
  handlebars.registerHelper('createValidate', createValidate);
  handlebars.registerHelper('updateValidate', updateValidate);
  handlebars.registerHelper('isExistsInArr', isExistsInArr);
};
