config.module.rules.push({
    test: /\.css$/,
    use: [
        'style-loader',
        {loader: 'css-loader', options: {importLoaders: 1}},
        {
            loader: 'postcss-loader',
            options: {
                ident: 'postcss',
                plugins: (loader) => [
                    require('postcss-import')({root: loader.resourcePath}),
                    require('postcss-preset-env')(),
                    require('cssnano')()
                ]
            }
        }
    ]
});


