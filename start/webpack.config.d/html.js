const HtmlWebpackPlugin = require("html-webpack-plugin");

config.output.publicPath = "";
config.plugins.push(new HtmlWebpackPlugin({
    title: "nimble",
    meta: {
        // basic
        ////
        viewport: "width=device-width, initial-scale=1, shrink-to-fit=no",
        description: "build a webapp with kotlin",
    }
}));
