//
//  MMAsyncLoadManager.m
//  ReactNativeAsyncLoadBundle
//
//  Created by Marcus on 2020/3/19.
//  Copyright © 2020 Facebook. All rights reserved.
//

#import "MMAsyncLoadManager.h"
#import "RCTBridge.h"
#import <React/RCTBridge+Private.h>

static MMAsyncLoadManager *instance;

@implementation MMAsyncLoadManager

+ (MMAsyncLoadManager *) getInstance{
  @synchronized(self) {
    if (!instance) {
      instance = [[self alloc] init];
    }
  }
  return instance;
}

-(instancetype) init{
  self.commonBundleExtension = @"bundle";
  self.commonBundleName = @"common.ios";
  return self;
}

-(void) prepareReactNativeEnv{
  NSDictionary *launchOptions = [[NSDictionary alloc] init];
  self.bridge = [[RCTBridge alloc] initWithDelegate:self
                                      launchOptions:launchOptions];
}

-(void) prepareReactNativeEnvWithCommonBundleName:(NSString*) fileName withExtension:(NSString*) extension{
  self.commonBundleName = fileName;
  self.commonBundleExtension = extension;
  [self prepareReactNativeEnv];
}

-(RCTRootView*) buildRootViewWithDiffBundleName:(NSString*) fileName withExtension:(NSString*) extension{
  NSURL *diffFileURL = [[NSBundle mainBundle] URLForResource: fileName withExtension:extension];
  NSData * diffData = [NSData dataWithContentsOfURL:diffFileURL];
  [self.bridge.batchedBridge  executeSourceCode:diffData sync:NO];
  RCTRootView *rootView = [[RCTRootView alloc] initWithBridge:self.bridge
                                                   moduleName:@"RNAsyncLoader"
                                            initialProperties:nil];
  rootView.backgroundColor = [[UIColor alloc] initWithRed:1.0f green:1.0f blue:1.0f alpha:1];
  return rootView;
}

- (NSURL *)sourceURLForBridge:(RCTBridge *)bridge
{
  return  [[NSBundle mainBundle] URLForResource:self.commonBundleName
                                  withExtension:self.commonBundleExtension];
}

@end
