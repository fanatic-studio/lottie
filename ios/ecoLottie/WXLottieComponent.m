//
//  WXLottieComponent.m
//

#import "WXLottieComponent.h"
#import <WeexPluginLoader/WeexPluginLoader.h>
#import "DeviceUtil.h"
#import "AFNetworking.h"

@implementation WXLottieComponent
@synthesize weexInstance;

WX_PlUGIN_EXPORT_COMPONENT(lottie, WXLottieComponent)
WX_EXPORT_METHOD(@selector(setSrc:))
WX_EXPORT_METHOD(@selector(setLoop:))
WX_EXPORT_METHOD(@selector(setSpeed:))
WX_EXPORT_METHOD(@selector(setContent:))
WX_EXPORT_METHOD(@selector(play))
WX_EXPORT_METHOD(@selector(pause))
WX_EXPORT_METHOD(@selector(resume))
WX_EXPORT_METHOD(@selector(stop))

- (instancetype)initWithRef:(NSString *)ref type:(NSString *)type styles:(NSDictionary *)styles attributes:(NSDictionary *)attributes events:(NSArray *)events weexInstance:(WXSDKInstance *)weexInstance {
    if (self = [super initWithRef:ref type:type styles:styles attributes:attributes events:events weexInstance:weexInstance]) {
        self.weexInstance = weexInstance;
        _loop = YES;
        _speed = 1.0f;

        for (NSString *key in styles.allKeys) {
            [self dataKey:key value:styles[key] isUpdate:NO];
        }
        for (NSString *key in attributes.allKeys) {
            [self dataKey:key value:attributes[key] isUpdate:NO];
        }
    }
    return self;
}

- (UIView *)loadView {
    return (UIView *) [LOTAnimationView new];
}

- (void)updateStyles:(NSDictionary *)styles {
    for (NSString *key in styles.allKeys) {
        [self dataKey:key value:styles[key] isUpdate:YES];
    }
}

- (void)updateAttributes:(NSDictionary *)attributes {
    for (NSString *key in attributes.allKeys) {
        [self dataKey:key value:attributes[key] isUpdate:YES];
    }
}

#pragma mark data

- (void)dataKey:(NSString *)key value:(id)value isUpdate:(BOOL)isUpdate {
    key = [DeviceUtil convertToCamelCaseFromSnakeCase:key];
    if ([key isEqualToString:@"eco"] && [value isKindOfClass:[NSDictionary class]]) {
        NSArray *array = [value allKeys];
        for (NSString *k in array) {
            [self dataKey:k value:value[k] isUpdate:isUpdate];
        }
    } else if ([key isEqualToString:@"src"] || [key isEqualToString:@"url"]) {
        _src = [WXConvert NSString:value];
        if (isUpdate) {
            [self setSrc:_src];
        }
    } else if ([key isEqualToString:@"loop"]) {
        _loop = [WXConvert BOOL:value];
        if (isUpdate) {
            [self setLoop:_loop];
        }
    } else if ([key isEqualToString:@"speed"]) {
        _speed = [WXConvert CGFloat:value];
        if (isUpdate) {
            [self setSpeed:_speed];
        }
    } else if ([key isEqualToString:@"content"]) {
        _content = [WXConvert NSString:value];
        if (isUpdate) {
            [self setContent:_content];
        }
    }
}

- (void)viewDidLoad {
    [self setSrc:_src];
    [self setLoop:_loop];
    [self setSpeed:_speed];
    [self setContent:_content];
    [self fireEvent:@"ready" params:nil];
}

- (void)viewWillUnload {
    [[self getLottie] setAnimationProgress:0.f];
    [[self getLottie] pause];
    [super viewWillUnload];
}

- (void)setSrc:(NSString *)src {
    if (_src.length == 0) {
        return;
    }
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    NSString *newSrc = [[self getUrl:src] absoluteString];
    [self fireEvent:@"stateChanged" params:@{@"src": newSrc, @"status": @"loading"}];
    [manager GET:newSrc parameters:@{} headers:nil progress:nil success:^(NSURLSessionDataTask *_Nonnull task, id _Nullable responseObject) {
        @try {
            if (responseObject) {
                [self setContent:responseObject];
                [self fireEvent:@"stateChanged" params:@{@"src": newSrc, @"status": @"success"}];
            } else {
                [self fireEvent:@"stateChanged" params:@{@"src": newSrc, @"status": @"error"}];
            }
        } @catch (NSException *exception) {
            [self fireEvent:@"stateChanged" params:@{@"src": newSrc, @"status": @"error"}];
        }
    } failure:nil];
}

- (void)setLoop:(BOOL)val {
    [[self getLottie] setLoopAnimation:val];
}

- (void)setSpeed:(CGFloat)val {
    [[self getLottie] setAnimationSpeed:val];
}

- (void)setContent:(id)val {
    if (val) {
        if ([val isKindOfClass:[NSDictionary class]]) {
            [[self getLottie] setAnimationFromJSON:val];
            [[self getLottie] play];
        } else if ([val isKindOfClass:[NSString class]]) {
            [[self getLottie] setAnimationFromJSON:[DeviceUtil dictionaryWithJsonString:val]];
            [[self getLottie] play];
        }
    }
}

- (void)play {
    [[self getLottie] setAnimationProgress:0.f];
    [[self getLottie] play];
}

- (void)pause {
    [[self getLottie] pause];
}

- (void)resume {
    [[self getLottie] play];
}

- (void)stop {
    [[self getLottie] stop];
}

- (LOTAnimationView *)getLottie {
    return (LOTAnimationView *) self.view;
}

- (NSURL *)getUrl:(NSString *)src {
    return [NSURL URLWithString:[DeviceUtil rewriteUrl:src mInstance:weexInstance]];
}

@end
