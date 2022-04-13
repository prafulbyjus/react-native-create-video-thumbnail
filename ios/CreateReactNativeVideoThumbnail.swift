import AVFoundation
import UIKit

@objc(CreateReactNativeVideoThumbnail)
class CreateReactNativeVideoThumbnail: NSObject {

    @objc(multiply:withB:withResolver:withRejecter:)
    func multiply(a: Float, b: Float, resolve:RCTPromiseResolveBlock,reject:RCTPromiseRejectBlock) -> Void {
        resolve(a*b)
    }

    @objc(getVideoThumbnail:withC:withResolver:withRejecter:)
    func getVideoThumbnail(url: String, fileName: String, resolve:RCTPromiseResolveBlock,reject:RCTPromiseRejectBlock) -> Void {
            if let imageURL = URL(string: url),  let thumbnailImage = getThumbnailImage(forUrl: imageURL) {
                
                let localPath = saveImage(image: thumbnailImage, fileName: fileName)
                resolve(localPath)

            } else {
                print("error")
                reject("", "",URLError.init(.badURL, userInfo: ["error":"invalid URL"]))
            }
    }
    
    func getThumbnailImage(forUrl url: URL) -> UIImage? {
            let asset: AVAsset = AVAsset(url: url)
            let imageGenerator = AVAssetImageGenerator(asset: asset)

            do {
                let thumbnailImage = try imageGenerator.copyCGImage(at: CMTimeMake(value: 5, timescale: 60), actualTime: nil)
                return UIImage(cgImage: thumbnailImage)
            } catch let error {
                print(error)
            }

            return nil
        }
    
    func saveImage(image: UIImage, fileName: String) -> Any {
            
            guard let data = image.jpegData(compressionQuality: 1) ?? image.pngData() else {
                return false
            }
            guard let directory = try? FileManager.default.url(for: .documentDirectory, in: .userDomainMask, appropriateFor: nil, create: false) as NSURL else {
                return false
            }
            let existingImageFilePath = getSavedImage(named: fileName)
        if existingImageFilePath.isEmpty {
            //add to storage
            do {
                try data.write(to: directory.appendingPathComponent(fileName + ".png")!)
                    let image = getSavedImage(named: fileName)
                        // do something with image
                        return image;
            } catch {
                print(error.localizedDescription)
                return false
            }
        } else {
            //fetch image
            print("in else", existingImageFilePath)
            return existingImageFilePath
        }
            
        }
    
    func getSavedImage(named: String) -> String {
            if let dir = try? FileManager.default.url(for: .documentDirectory, in: .userDomainMask, appropriateFor: nil, create: false) {
                let filePath = URL(fileURLWithPath: dir.absoluteString).appendingPathComponent(named).path
                return filePath+".png";
            }
        return "";
        }
    
}
