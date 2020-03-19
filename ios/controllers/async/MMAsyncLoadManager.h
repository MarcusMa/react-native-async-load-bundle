//
//  MMAsyncLoadManager.h
//  ReactNativeAsyncLoadBundle
//
//  Created by Marcus on 2020/3/19.
//  Copyright © 2020 Facebook. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <React/RCTBridge.h>
#import <React/RCTRootView.h>
#import <React/RCTBridgeDelegate.h>

NS_ASSUME_NONNULL_BEGIN

@interface MMAsyncLoadManager : NSObject<RCTBridgeDelegate>

@property (nonatomic, strong) RCTBridge *bridge;
@property (nonatomic, strong) NSString *commonBundleName;
@property (nonatomic, strong) NSString *commonBundleExtension;

+ (MMAsyncLoadManager *)getInstance;

-(void) prepareReactNativeEnv;

-(void) prepareReactNativeEnvWithCommonBundleName:(NSString*) fileName withExtension:(NSString*) extension;

-(RCTRootView*) buildRootViewWithDiffBundleName:(NSString*) fileName withExtension:(NSString*) extension;

@end

NS_ASSUME_NONNULL_END
