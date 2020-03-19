//
//  MMTimeUtils.m
//  ReactNativeAsyncLoadBundle
//
//  Created by Marcus on 2020/3/19.
//  Copyright © 2020 Facebook. All rights reserved.
//

#import "MMTimeRecordUtil.h"

static MMTimeRecordUtil *instance;

@implementation MMTimeRecordUtil

+ (MMTimeRecordUtil *)getInstance{
  @synchronized(self) {
    if (!instance) {
      instance = [[self alloc] init];
    }
  }
  return instance;
};

-(instancetype) init{
  self.recordDic = [[NSMutableDictionary alloc] init];
  return self;
}

- (void)setStartTime:(NSString*) tag{
  NSMutableDictionary* record = [[NSMutableDictionary alloc] initWithCapacity:2];
  [record setObject:@([self getCurrentTimestamp]) forKey:@"startTime"];
  [record setObject:@(0) forKey:@"endTime"];
  [self.recordDic setObject:record forKey:tag];
};
- (void)setEndTime:(NSString*) tag{
  NSMutableDictionary* record = [self.recordDic objectForKey:tag];
  if(nil != record){
    [record setObject:@([self getCurrentTimestamp]) forKey:@"endTime"];
  }
};

- (long) getTimeCost:(NSString*) tag{
  NSMutableDictionary* record = [self.recordDic objectForKey:tag];
  if(nil != record){
    return [[record objectForKey:@"endTime"] longValue] - [[record objectForKey:@"startTime"] longValue];
  }else{
    return -1.0;
  }
};

- (NSString*)getReadableTimeCostWithUnit:(NSString*) tag{
  if([self isFinished:tag]){
    return [[NSString alloc] initWithFormat:@"%ld ms",[self getTimeCost:tag]];
  }else{
    return @"Unfinished.";
  }
};

- (void)printTimeInfo:(NSString*) tag{
  NSMutableDictionary* record = [self.recordDic objectForKey:tag];
  if(nil != record){
    if([self isFinished:tag]){
      NSLog(@"----------------------------------");
      NSLog(@"| Tag : %@", tag);
      NSLog(@"| Start Time (ms): %ld", [[record objectForKey:@"startTime"] longValue]);
      NSLog(@"| End Time (ms): %ld", [[record objectForKey:@"endTime"] longValue]);
      NSLog(@"| Time cost (ms): %ld", [self getTimeCost:tag]);
      NSLog(@"----------------------------------");
    }else{
      NSLog(@"----------------------------------");
      NSLog(@"| Unfinished TimeRecord For %@",tag);
      NSLog(@"----------------------------------");
    }
  }else{
    NSLog(@"----------------------------------");
    NSLog(@"| No TimeRecord For %@",tag);
    NSLog(@"----------------------------------");
  }
};

- (BOOL)isFinished:(NSString*) tag{
  NSMutableDictionary* record = [self.recordDic objectForKey:tag];
  if(nil != record){
    return [[record objectForKey:@"endTime"] doubleValue] > 1 && [[record objectForKey:@"startTime"] doubleValue] > 1;
  }else{
    return NO;
  }
};

- (NSTimeInterval)getCurrentTimestamp {
  NSDate *date = [NSDate dateWithTimeIntervalSinceNow:0];
  NSTimeInterval time = [date timeIntervalSince1970]*1000;
  return time;
}


@end
