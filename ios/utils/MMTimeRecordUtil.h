//
//  MMTimeUtils.h
//  ReactNativeAsyncLoadBundle
//
//  Created by Marcus on 2020/3/19.
//  Copyright © 2020 Facebook. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface MMTimeRecordUtil : NSObject

@property (nonatomic, strong) NSMutableDictionary *recordDic;

+ (MMTimeRecordUtil *)getInstance;

- (void)setStartTime:(NSString*) tag;
- (void)setEndTime:(NSString*) tag;
- (void)printTimeInfo:(NSString*) tag;
- (long)getTimeCost:(NSString*) tag;
- (NSString*)getReadableTimeCostWithUnit:(NSString*) tag;
- (BOOL)isFinished:(NSString*) tag;

@end

NS_ASSUME_NONNULL_END
