//
//  WXLottieComponent.h
//

#import "WXComponent.h"
#import <WeexSDK/WXEventModuleProtocol.h>
#import <WeexSDK/WXModuleProtocol.h>
#import <Lottie/Lottie.h>

NS_ASSUME_NONNULL_BEGIN

@interface WXLottieComponent : WXComponent <WXModuleProtocol>

@property(strong, nonatomic) NSString *src;
@property(nonatomic, assign) BOOL loop;
@property(nonatomic, assign) CGFloat speed;
@property(strong, nonatomic) NSString *content;

@end

NS_ASSUME_NONNULL_END
