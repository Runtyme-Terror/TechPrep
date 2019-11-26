var mongoose = require('mongoose');
var Schema = mongoose.Schema;
var random = require('mongoose-simple-random');

var technicalSchema = new Schema({
	question: {type: String, required: true},
	author: {type: String, required: true}},
	 {collection: 'technicalQuestions'});
technicalSchema.plugin(random);

var technical = mongoose.model('technical', technicalSchema);
module.exports = technical;