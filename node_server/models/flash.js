var mongoose = require('mongoose');
var Schema = mongoose.Schema;
var random = require('mongoose-simple-random');

var flashSchema = new Schema({
	question: {type: String, required: true},
	answer: {type: String, required: true},
	author: {type: String, required: true}},
	{collection: 'flashQuestions'});
flashSchema.plugin(random);


var flash = mongoose.model('flash', flashSchema);
module.exports = flash;