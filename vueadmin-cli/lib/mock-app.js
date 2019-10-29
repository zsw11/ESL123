'use strict';

module.exports = {
  Sequelize: {
    INTEGER: { typeId: 'integer' },
    TEXT: { typeId: 'text' },
    BOOLEAN: { typeId: 'boolean' },
    DATE: { typeId: 'date' },
    DATEONLY: { typeId: 'dateOnly' },
    JSON: { typeId: 'json' },
    STRING: precision => { return { typeId: 'varchar', precision }; },
    DECIMAL: (precision, scale) => { return { typeId: 'decimal', precision, scale }; },
  },
  model: {
    define(name, attrs, options) {
      module.exports.models[name] = {
        tableName: name,
        attrs,
        options,
      };
      return module.exports.models[name];
    }
  },
  models: [],
};
