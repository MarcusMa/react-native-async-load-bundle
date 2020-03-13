const fs = require('fs');
const readline = require('readline');
// Functions

// Main
let argvs = process.argv.splice(2);
let filePath = argvs[0];

var fRead = fs.createReadStream(filePath);
var objReadline = readline.createInterface({
  input: fRead,
});
let diff = new Array();
objReadline.on('line', function(line) {
  if (line.startsWith('__d') || line.startsWith('__r')) {
    diff.push(line);
  }
});
objReadline.on('close', function() {
  let data = diff.join('\n');
  fs.writeFileSync(filePath, data); // 生成 diff.ios.jsbundle
});
