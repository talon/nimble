const HtmlWebpackPlugin = require('html-webpack-plugin');

config.output.publicPath = "";
config.plugins.push(new HtmlWebpackPlugin({
    title: "secret specter",
    meta: {
        // basic
        ////
        viewport: "width=device-width, initial-scale=1, shrink-to-fit=no",
        description: "a specter haunts this website",
        // open-graph
        // http://ogp.me/
        ////
        "og:type": "website",
        "og:url": "https://theghostin.me",
        "og:title": "ghost",
        "og:description": "a specter haunts this website",
        // twitter cards
        ////
        "twitter:card": "summary",
        "twitter:site": "@secretspecter",
        "twitter:creator": "@secretspecter"
    }
}));