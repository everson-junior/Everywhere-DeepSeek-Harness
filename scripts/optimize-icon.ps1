Add-Type -AssemblyName System.Drawing

$src = Resolve-Path "assets/icon/icon.jpg"
$img = [System.Drawing.Image]::FromFile($src)

# Redimensiona para 256x256 (tamanho padrão ideal e recomendado para o VS Code Marketplace)
$size = 256
$bmp = New-Object System.Drawing.Bitmap($size, $size)
$graph = [System.Drawing.Graphics]::FromImage($bmp)
$graph.InterpolationMode = [System.Drawing.Drawing2D.InterpolationMode]::HighQualityBicubic
$graph.SmoothingMode = [System.Drawing.Drawing2D.SmoothingMode]::HighQuality
$graph.PixelOffsetMode = [System.Drawing.Drawing2D.PixelOffsetMode]::HighQuality
$graph.DrawImage($img, 0, 0, $size, $size)

$destinations = @(
    "assets/icon/icon.png",
    "vscode/assets/icon/icon.png",
    "visualstudio/Everywhere/Resources/Icon.png"
)

foreach ($dest in $destinations) {
    $fullDest = Join-Path $PWD $dest
    # Salva temporário e substitui para liberar handles
    $tempPath = "$fullDest.tmp"
    $bmp.Save($tempPath, [System.Drawing.Imaging.ImageFormat]::Png)
    Move-Item -Path $tempPath -Destination $fullDest -Force
    $sizeKb = [math]::Round(((Get-Item $fullDest).Length / 1KB), 1)
    Write-Host "✅ Otimizado $dest -> $sizeKb KB"
}

$img.Dispose()
$bmp.Dispose()
$graph.Dispose()
