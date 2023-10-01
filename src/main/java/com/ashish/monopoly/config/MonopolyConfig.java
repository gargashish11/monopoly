package com.ashish.monopoly.config;


//@Configuration
//public class MonopolyConfig {
//
//    @Bean
//    public GameService gameService() {
//        return new DefaultGameService();
//    }
//
//    @Bean
//    public PlayerService playerService() {
//        return new DefaultPlayerService();
//    }
//
//    @Bean
//    public ConversionService conversionService() {
//        var conversionService = new GenericConversionService();
//        conversionService.addConverter(new GamePlayerConverter());
//        conversionService.addConverter(new TransactionConverter());
//        conversionService.addConverter(new GameConverter(conversionService));
//
//        conversionService.addConverter(new GamePlayerReverseConverter());
//        conversionService.addConverter(new TransactionReverseConverter(gameService(), playerService()));
//        conversionService.addConverter(new GameReverseConverter(conversionService));
//        return conversionService;
//    }
//}
