var telegram = require('telegram-bot-api');
var request = require('request');
var summarizer = require('nodejs-text-summarizer');
var htmlToText = require('html-to-text');
var YandexTranslator = require('yandex.translate');
var translate = require('yandex-translate')('trnsl.1.1.20160814T030040Z.8d320379d747e007.8104bd6ee8d6f3b0a27556f65d33d736a04a2d7c');
var latitude = '';
var longitude = '';
var mood = 'leisure-outdoor';
var api = new telegram({
    token: '198085265:AAFygFU7n37DMDSRZPnRVpnkpK6s2zEA4jo',
    updates: {
        enabled: true,
        get_interval: 1000
    }
});
var llll = '';
var ak = 'b7d11214c8fc452db3de12028cf46daa';
var sk = '64631fe987f4423bb0a117101bf90a45'
var ocr = require('./ocr.js').create(ak, sk);
// var havenondemand = require('havenondemand');
// var client = new havenondemand.HODClient(apikey, version, proxy);

var sentimentAnalysis = require('haven-sentiment-analysis');

var apiKey = '5f086ef4-def4-40e4-b284-cadf3be894e3';

var Twit = require('twit')

var T = new Twit({
    consumer_key: 'IkXDDWXttRRpdEufh5ULG033I',
    consumer_secret: '6au14N71hL2exMoiL8d5UPpnrneCySHW3Wkq6lpkQPF25Fjlgp',
    access_token: '718826975142354944-9BSOEZ5uUkUtlJrV9tneApTknRsQZDY',
    access_token_secret: 'fuSDbCUhgi4eZAkV5UboSWMHbP5rif9iNswDx0P6LYuaZ'
})


api.on('message', function(message) {
    var chat_id = message.chat.id;
    if (message.photo != undefined) {
        length = message.photo.length;
        console.log(message.photo[length - 1].file_id);

        request({
            url: 'https://api.telegram.org/bot198085265:AAFygFU7n37DMDSRZPnRVpnkpK6s2zEA4jo/getFile?file_id=' + message.photo[length - 1].file_id,
            json: true
        }, function(error, response, body) {
            if (!error && response.statusCode == 200) {


                // ocr.scan({
                //         type: 'text',
                //     }).then(function(result) {

                //         translate.translate(result.results.words, { to: 'en' }, function(err, res) {
                //             var a = res.text;
                //             console.log(typeof(a));
                //         });
                //     }).catch(function(err) {
                //         console.log('err', err);
                //     })
                llll = 'https://api.telegram.org/file/bot198085265:AAFygFU7n37DMDSRZPnRVpnkpK6s2zEA4jo/' + body.result.file_path;
            }
        })




    }

    if (message.text != undefined) {
        if (message.text == 'Search a place for me') {
            api.sendMessage({
                    chat_id: message.chat.id,

                    text: message.text ? 'What would you like to search for? \n eat-drink\n restaurant \n coffee-tea\n snacks-fast-food \n going-out\n sights-museums\n transport \n airport \n accommodation \n shopping \n leisure-outdoor \n  administrative-areas-buildings \n natural-geographical \n petrol-station \n atm-bank-exchange \n toilet-rest-area \n hospital-health-care-facility' : 'This message doesn\'t contain text :('
                })
                .then(function(message) {
                    console.log(message);
                })
                .catch(function(err) {
                    console.log(err);
                });
            api.on('message', function(message) {
                // mood @ New Delhi
                arr = message.text.split("@");
                mood = arr[0];
                place = arr[1];
                console.log('Somebody came here with a mood' + mood);
                request({
                    url: 'https://geocoder.cit.api.here.com/6.2/geocode.xml?app_id=qWWMtPTtoZAUNjN8FRqj&app_code=_Vidf-iou_-XwDu-27uF8A&gen=9&searchtext=' + place,
                    json: true
                }, function(error, response, body) {
                    if (!error && response.statusCode == 200) {
                        var array = body.split("<Latitude>");
                        var array1 = array[1].split("</Latitude>");
                        var array2 = array1[1].split("<Longitude>");
                        var array3 = array2[1].split("</Longitude>");
                        latitude = array1[0];
                        longitude = array3[0];

                        console.log('lat: ' + latitude + ' long: ' + longitude);
                        request({
                            url: 'https://places.demo.api.here.com/places/v1/discover/explore?at=' + latitude + ',' + longitude + '&cat=' + mood + '&app_id=qWWMtPTtoZAUNjN8FRqj&app_code=_Vidf-iou_-XwDu-27uF8A',
                            json: true
                        }, function(error, response, body) {
                            if (!error && response.statusCode == 200) {
                                console.log(body.results.items);
                                var k = body.results.items
                                for (var i = 0; i < k.length; i++) {

                                    api.sendMessage({
                                            chat_id: message.chat.id,
                                            text: message.text ? body.results.items[i].title : 'This message doesn\'t contain text :('
                                        })
                                        .then(function(message) {
                                            console.log(message);
                                        })
                                        .catch(function(err) {
                                            console.log(err);
                                        });

                                }

                            }
                        })
                    }

                })
            })
        } else if (message.text.search("https://www.youtube.com") != -1) {
            var video = message.text.replace("https://www.youtube.com/watch?v=", '');
            request({
                url: 'https://www.youtube.com/api/timedtext?v=' + video + '&lang=en',
                json: true
            }, function(error, response, body) {
                if (!error && response.statusCode == 200) {
                    var text = htmlToText.fromString(body, {
                        wordwrap: 130
                    });
                    var opt = {
                        n: 5,
                        lang: 'ID',

                    }
                    var result = summarizer(text, opt);
                    api.sendMessage({
                            chat_id: message.chat.id,
                            text: message.text ? result : 'This message doesn\'t contain text :('
                        })
                        .then(function(message) {
                            // console.log(message);
                        })
                        .catch(function(err) {
                            // console.log(err);
                        });

                    var data = { 'text': result }
                    sentimentAnalysis(result, '169c99ef-0df2-4cd6-85c6-4da5145b9a2a', function(results) {
                        console.log(results.aggregate.sentiment); // Do some awesome stuff with sentiment data!
                        if (results.aggregate.score >= 0.7) {
                            api.sendMessage({
                                    chat_id: message.chat.id,
                                    text: message.text ? 'Highly recommeded' : 'This message doesn\'t contain text :('
                                })
                                .then(function(message) {
                                    console.log(message);
                                })
                                .catch(function(err) {
                                    console.log(err);
                                });
                        } else if (results.aggregate.score >= 0.4) {
                            api.sendMessage({
                                    chat_id: message.chat.id,
                                    text: message.text ? 'Moderately recommeded' : 'This message doesn\'t contain text :('
                                })
                                .then(function(message) {
                                    console.log(message);
                                })
                                .catch(function(err) {
                                    console.log(err);
                                });
                        } else if (results.aggregate.score >= 0) {
                            api.sendMessage({
                                    chat_id: message.chat.id,
                                    text: message.text ? 'Mixed Suggestion' : 'This message doesn\'t contain text :('
                                })
                                .then(function(message) {
                                    console.log(message);
                                })
                                .catch(function(err) {
                                    console.log(err);
                                });
                        } else if (results.aggregate.score <= 0) {
                            api.sendMessage({
                                    chat_id: message.chat.id,
                                    text: message.text ? 'Not recommeded' : 'This message doesn\'t contain text :('
                                })
                                .then(function(message) {
                                    console.log(message);
                                })
                                .catch(function(err) {
                                    console.log(err);
                                });
                        }
                        // api.sendMessage({
                        //         chat_id: message.chat.id,
                        //         text: message.text ? results.aggregate.sentiment : 'This message doesn\'t contain text :('
                        //     })
                        //     .then(function(message) {
                        //         console.log(message);
                        //     })
                        //     .catch(function(err) {
                        //         console.log(err);
                        //     });
                    });
                    // client.post('analyzesentiment', callback, data)

                    console.log("result==>\n" + result);
                    // console.log(sentiment);
                }
            })
        } else if (message.text == 'translate to english') {
            ocr.scan({
                url: llll,
                type: 'text',
            }).then(function(result) {

                translate.translate(result.results.words, { to: 'en' }, function(err, res) {
                    var a = res.text;
                    console.log(a);
                    api.sendMessage({
                            chat_id: message.chat.id,
                            text: message.text ? a : 'This message doesn\'t contain text :('
                        })
                        .then(function(message) {
                            console.log(message);
                        })
                        .catch(function(err) {
                            console.log(err);
                        });
                });
            }).catch(function(err) {
                console.log('err', err);
            })
        } else if (message.text == 'translate to hindi') {
            ocr.scan({
                url: llll,
                type: 'text',
            }).then(function(result) {

                translate.translate(result.results.words, { to: 'hi' }, function(err, res) {
                    var a = res.text;
                    console.log(a);
                    api.sendMessage({
                            chat_id: message.chat.id,
                            text: message.text ? a : 'This message doesn\'t contain text :('
                        })
                        .then(function(message) {
                            console.log(message);
                        })
                        .catch(function(err) {
                            console.log(err);
                        });
                });
            }).catch(function(err) {
                console.log('err', err);
            })
        } else if (message.text.search("tweet") == 0) {
            var array3 = message.text.split("tweet");
            T.post('statuses/update', { status: array3[1] }, function(err, data, response) {
                api.sendMessage({
                        chat_id: message.chat.id,
                        text: message.text ? 'Succesfully Tweeted :)' : 'This message doesn\'t contain text :('
                    })
                    .then(function(message) {
                        console.log(message);
                    })
                    .catch(function(err) {
                        console.log(err);
                    });
            })
        }
    }
});
