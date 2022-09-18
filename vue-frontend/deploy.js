const {Storage} = require('@google-cloud/storage');

const fileName = 'app';
const bucketName = 'webpack-frontend';
const storage = new Storage({
  keyFilename: './google-secret.json',
  projectId: 'crested-polygon-362000'
});
const projectBucket = storage.bucket(bucketName);

const uploadFiles = target => {
  return projectBucket.upload(`./dist/static/${target}`);
};

const deploy = (fn, arr = []) => {
  if (arr.length === 0) {
    return;
  }
  let ele = arr.shift();
  fn(ele).then(data => {
    console.log(data);
    deploy(fn, arr);
  }).catch(console.log);
};

deploy(uploadFiles, [`js/${fileName}.js`, `css/${fileName}.css`])
